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


/**
 * Created by Jeffrey on 2016-12-19.
 */
public class WatAccount {
    private WatSession session;
    private String account;
    private char[] password;
    private List<WatBalance> balances;

    public WatAccount(WatSession session, String account, String password) {
        this.session = session;
        this.account = account;
        this.password = password.toCharArray();
    }

    public void newSession() {
        session = new WatSession();
    }

    public int login() {

        final String LOGIN_URL = "https://watcard.uwaterloo.ca/OneWeb/Account/LogOn";
        int code = -1;

        HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(session.getCookieStore()).build();
        HttpPost post = new HttpPost(LOGIN_URL);


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

    public List<WatBalance> getBalances() {
        return balances;
    }

    public void loadBalances() {
        final String BALANCE_URL = "https://watcard.uwaterloo.ca/OneWeb/Financial/Balances";
        balances = new ArrayList<>();

        HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(session.getCookieStore()).build();
        HttpGet get = new HttpGet(BALANCE_URL);

        try {
            HttpResponse response = client.execute(get);
            String html_response = new BasicResponseHandler().handleResponse(response);

            Document doc = Jsoup.parse(html_response);
            Element content = doc.getElementsByClass("col-md-9").first();
            Elements rows = content.select("tbody");


            for (Element row: rows) {
                Elements columns = row.select("td");

                int id = Integer.parseInt(columns.get(0).text());
                String name = columns.get(1).text();
                double limit = Double.parseDouble(columns.get(2).text().replace("$", ""));
                double value = Double.parseDouble(columns.get(3).text().replace("$", ""));

                balances.add(new WatBalance(id, name, limit, value));
            }
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }
    }
}
