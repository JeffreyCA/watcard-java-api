package ca.jeffrey.watcard.api;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class WatAccount {
    private WatSession session;
    private String account;
    private char[] password;
    private List<WatBalance> balances;

    private String name;
    private String birth_date;
    private String marital_status;
    private String sex;
    private String email;
    private String phone;
    private String mobile;
    private String address;

    public WatAccount(WatSession session, String account, String password) {
        this.session = session;
        this.account = account;
        this.password = password.toCharArray();
        balances = new ArrayList<>();
        name = birth_date = marital_status = sex = email = phone = mobile = address = "";
    }

    public void newSession() {
        session = new WatSession();
    }

    public List<WatBalance> getBalances() {
        return balances;
    }

    public int login() {
        final String LOGIN_URL = "https://watcard.uwaterloo.ca/OneWeb/Account/LogOn";
        int code = -1;

        // Set cookie store
        HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(session.getCookieStore()).build();
        HttpPost post = new HttpPost(LOGIN_URL);

        // Parameters to send
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("__RequestVerificationToken", session.getVerificationToken()));
        urlParameters.add(new BasicNameValuePair("AccountMode", "0"));
        urlParameters.add(new BasicNameValuePair("Account", account));
        urlParameters.add(new BasicNameValuePair("Password", new String(password)));

        try {
            post.setEntity(new UrlEncodedFormEntity(urlParameters));
            HttpResponse response = client.execute(post);
            code = response.getStatusLine().getStatusCode();
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }
        return code;
    }

    // Retrieve balance data and store in balances list
    public void loadBalances() {
        final String BALANCE_URL = "https://watcard.uwaterloo.ca/OneWeb/Financial/Balances";
        balances = new ArrayList<>();

        HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(session.getCookieStore()).build();
        HttpGet get = new HttpGet(BALANCE_URL);

        try {
            HttpResponse response = client.execute(get);
            String html_response = new BasicResponseHandler().handleResponse(response);

            Document doc = Jsoup.parse(html_response);
            Elements accounts = doc.getElementsByClass("table table-striped ow-table-responsive").first()
                    .select("tbody").first().select("tr");

            for (Element balance: accounts) {
                Elements info = balance.select("td");
                String id = info.get(0).text();
                String name = info.get(1).text();
                // Remove $ sign
                double limit = Double.parseDouble(info.get(2).text().replace("$", ""));
                double value = Double.parseDouble(info.get(3).text().replace("$", ""));

                balances.add(new WatBalance(id, name, limit, value));
            }
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    // TODO: Cache html response
    public void loadPersonalInfo() {
        final String BALANCE_URL = "https://watcard.uwaterloo.ca/OneWeb/Account/Personal";
        balances = new ArrayList<>();

        HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(session.getCookieStore()).build();
        HttpGet get = new HttpGet(BALANCE_URL);

        try {
            HttpResponse response = client.execute(get);
            String html_response = new BasicResponseHandler().handleResponse(response);

            Document doc = Jsoup.parse(html_response);
            Elements info = doc.getElementsByClass("ow-info-container").first()
                    .select("span.ow-value");

            name = info.get(0).text().replaceAll("\\.", ""); // Remove unwanted period character
            birth_date = info.get(2).text();
            marital_status = info.get(3).text();
            sex = info.get(4).text();
            email = info.get(5).text();
            phone = info.get(6).text().replaceAll("[-().\\s]", ""); // Remove all formatting
            mobile =  info.get(7).text().replaceAll("[-().\\s]", "");
            address =  info.get(8).text();
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    // Clear, formatted output of data
    public void displayBalances() {
        if (balances != null) {
            for (WatBalance b: balances) {
                System.out.printf("%s %s: $%.2f%n", b.getId(), b.getName(), b.getValue());
            }
        }
    }

    public void displayPersonalInfo() {
        System.out.printf("Name: %s%nBirth date: %s%nMarital Status: %s%nSex: %s%nEmail: %s%nPhone: %s%n" +
                "Mobile: %s%nAddress: %s%n", name, birth_date, marital_status, sex, email, phone, mobile, address);
    }
}
