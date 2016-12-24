package ca.jeffrey.watcard;

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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;


public class WatAccount {

    // Number of balance types
    private final int BALANCE_TYPES = 12;

    // Main fields
    private WatSession session;
    private String account;
    private char[] password;
    private List<WatBalance> balances;

    // Personal information fields
    private String name;
    private String birthDate;
    private String maritalStatus;
    private String sex;
    private String email;
    private String phone;
    private String mobile;
    private String address;

    /**
     * Constructor
     *
     * @param session  a WatSession
     * @param account  student id
     * @param password associated password
     */
    public WatAccount(WatSession session, String account, String password) {
        this.session = session;
        this.account = account;
        this.password = password.toCharArray();
        balances = new ArrayList<>();
        name = birthDate = maritalStatus = sex = email = phone = mobile = address = "";
    }

    /**
     * Constructor
     *
     * @param account  student id
     * @param password associated password
     */
    public WatAccount(String account, String password) {
        this.session = new WatSession();
        this.account = account;
        this.password = password.toCharArray();
        balances = new ArrayList<>();
        name = birthDate = maritalStatus = sex = email = phone = mobile = address = "";
    }

    /**
     * Load new WatSession.
     */
    public void newSession() {
        session = new WatSession();
    }

    /**
     * Logs user into WatCard site by initiating a POST request containing a {@code __RequestVerificationToken} and user
     * account details. Uses a {@code WatSession} to store cookies and verification token.
     *
     * @return Response of POST request, or -1 if there was an IOException
     */
    public int login() {
        // Request URL
        final String LOGIN_URL = "https://watcard.uwaterloo.ca/OneWeb/Account/LogOn";
        // Default code
        int code = -1;

        // Set cookie store
        HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(session.getCookieStore()).build();
        HttpPost post = new HttpPost(LOGIN_URL);

        // Parameters to send
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("__RequestVerificationToken", session.getVerificationToken()));
        urlParameters.add(new BasicNameValuePair("AccountMode", "0")); // default value
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

    /**
     * Retrieves user's account information stores it in {@code WatAccount} fields.
     */
    public void loadPersonalInfo() {
        // Request URL
        final String BALANCE_URL = "https://watcard.uwaterloo.ca/OneWeb/Account/Personal";

        // Use session's cookie store
        HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(session.getCookieStore()).build();
        HttpGet get = new HttpGet(BALANCE_URL);

        try {
            // Perform request
            HttpResponse response = client.execute(get);
            String htmlResponse = new BasicResponseHandler().handleResponse(response);
            Document doc = Jsoup.parse(htmlResponse);
            Elements info = doc.getElementsByClass("ow-info-container").first()
                    .select("span.ow-value");

            // Store selected data in corresponding fields
            name = info.get(0).text().replaceAll("\\.", ""); // Remove unwanted period character
            birthDate = info.get(2).text();
            maritalStatus = info.get(3).text();
            sex = info.get(4).text();
            email = info.get(5).text();
            phone = info.get(6).text().replaceAll("[-().\\s]", ""); // Remove all formatting
            mobile = info.get(7).text().replaceAll("[-().\\s]", "");
            address = info.get(8).text();
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Outputs account information.
     */
    public void displayPersonalInfo() {
        System.out.printf("Name: %s%nBirth date: %s%nMarital Status: %s%nSex: %s%nEmail: %s%nPhone: %s%n" +
                "Mobile: %s%nAddress: %s%n", name, birthDate, maritalStatus, sex, email, phone, mobile, address);
    }

    /**
     * Retrieves user's account balances and stores them in {@code balances}, a list of {@code WatBalance}.
     */
    public void loadBalances() {
        // Request URL
        final String BALANCE_URL = "https://watcard.uwaterloo.ca/OneWeb/Financial/Balances";
        // Initialize list
        balances = new ArrayList<>();

        // Use session's cookie store
        HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(session.getCookieStore()).build();
        HttpGet get = new HttpGet(BALANCE_URL);

        try {
            // Perform request
            HttpResponse response = client.execute(get);
            String htmlResponse = new BasicResponseHandler().handleResponse(response);
            Document doc = Jsoup.parse(htmlResponse);
            // Select rows in the balance table
            Elements accounts = doc.getElementsByClass("table table-striped ow-table-responsive").first()
                    .select("tbody").first().select("tr");

            // Iterate through each column in the row
            for (Element balance : accounts) {
                Elements info = balance.select("td");
                String id = info.get(0).text();
                String name = info.get(1).text();
                // Remove $ character
                double limit = Double.parseDouble(info.get(2).text().replace("$", ""));
                double value = Double.parseDouble(info.get(3).text().replace("$", ""));
                // Add WatBalance to list
                balances.add(new WatBalance(id, name, limit, value));
            }
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    /**
     * Outputs balances.
     */
    public void displayBalances() {
        for (WatBalance b : balances) {
            System.out.println(b);
        }
    }

    /**
     * Get account balances
     *
     * @return list of WatBalance
     */
    public List<WatBalance> getBalances() {
        return balances;
    }

    /**
     * Returns a {@code WatBalance} of the given balance type. If the balances were not properly loaded, it returns null.
     * @param type balance type
     * @return {@code WatBalance} of {@code type}
     */
    public WatBalance getWatBalance(WatBalanceType type) {
        WatBalance balance = null;

        if (balances.size() == BALANCE_TYPES) {
            switch (type) {
                case VILLAGE_MEAL:
                    balance = balances.get(0);
                    break;
                case BEST_BUY_MEAL:
                    balance = balances.get(1);
                    break;
                case FOOD_PLAN:
                    balance = balances.get(2);
                    break;
                case FLEX1:
                    balance = balances.get(3);
                    break;
                case FLEX2:
                    balance = balances.get(4);
                    break;
                case FLEX3:
                    balance = balances.get(5);
                    break;
                case TRANSFER:
                    balance = balances.get(6);
                    break;
                case DON_MEAL:
                    balance = balances.get(7);
                    break;
                case DON_FLEX:
                    balance = balances.get(8);
                    break;
                case REWARDS:
                    balance = balances.get(9);
                    break;
                case DEPT_CHARGE:
                    balance = balances.get(10);
                    break;
                case OVERDRAFT:
                    balance = balances.get(11);
                    break;
            }
        }
        return balance;
    }

    /**
     * Returns the amount of funds in the given balance type. If {@code balances} was not properly loaded, it returns 0.
     * @param type balance type
     * @return amount in the {@code type} balance
     */
    public double getWatBalanceValue(WatBalanceType type) {
        WatBalance balance = getWatBalance(type);

        if (balance == null) {
            return 0;
        }
        else {
            return balance.getValue();
        }
    }

    /**
     * Returns amount of Flex Dollars in account by adding up all three Flex accounts. If {@code balances} was not
     * properly loaded, it returns 0.
     *
     * @return amount of Flex Dollars
     */
    public double getFlexBalance() {
        double balance = 0;

        if (balances.size() == BALANCE_TYPES) {
            balance = getWatBalance(WatBalanceType.FLEX1).getValue() +
                    getWatBalance(WatBalanceType.FLEX2).getValue() +
                    getWatBalance(WatBalanceType.FLEX3).getValue();
        }
        return balance;
    }

    /**
     * Returns amount of meal plan funds. If {@code balances} was not properly loaded, it returns 0.
     * @return amount of funds in meal plan
     */
    public double getMealBalance() {
        double balance = 0;

        if (balances.size() == BALANCE_TYPES) {
            balance = getWatBalance(WatBalanceType.VILLAGE_MEAL).getValue() +
                    getWatBalance(WatBalanceType.BEST_BUY_MEAL).getValue() +
                    getWatBalance(WatBalanceType.FOOD_PLAN).getValue();
        }
        return balance;
    }

    /**
     * Returns a list of transactions from the given url.
     *
     * @param url request URL
     * @return list of WatTransaction
     */
    public List<WatTransaction> getTransactions(String url) {
        // DateTime format of transactions received from the requested data
        // Note: This format is different from the DateTime format that is passed in the url itself
        final DateTimeFormatter RESPONSE_FORMAT = DateTimeFormatter.ofPattern("MM/dd/yyyy h:mm:ss a");

        // Use session's cookie store
        HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(session.getCookieStore()).build();
        HttpGet get = new HttpGet(url);

        // Initialize list
        List<WatTransaction> transactions = new ArrayList<>();

        try {
            // Perform request
            HttpResponse response = client.execute(get);
            String htmlResponse = new BasicResponseHandler().handleResponse(response);

            if (!htmlResponse.contains("No transactions found!")) {
                Document doc = Jsoup.parse(htmlResponse);
                // Select table of transactions
                Elements row = doc.getElementsByClass("table table-striped ow-table-responsive").first()
                        .select("tbody").first().select("tr");

                for (Element transaction : row) {
                    // Elements containing transaction information
                    Elements data = transaction.select("td");
                    // Store selected data in corresponding fields
                    LocalDateTime dateTime = LocalDateTime.parse(data.get(0).text(), RESPONSE_FORMAT);
                    double amount = Double.valueOf(data.get(1).text().replace("$", ""));
                    String account = data.get(2).text();
                    int unit = Integer.valueOf(data.get(3).text());
                    String type = data.get(4).text();
                    String terminal = data.get(5).text();
                    // Add resulting WatTransaction to list
                    transactions.add(new WatTransaction(dateTime, amount, account, unit, type, terminal));
                }
            }
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }
        return transactions;
    }

    /**
     * Returns a list of transactions from a given date to now.
     *
     * @param begin starting date &amp; time
     * @return list of WatTransactions completed from {@code now} to now
     */
    public List<WatTransaction> getTransactions(LocalDateTime begin) {
        String formattedBegin = WatTransaction.DATE_FORMAT.format(begin);
        String formattedToday = WatTransaction.DATE_FORMAT.format(LocalDateTime.now());
        String url = WatTransaction.BASE_URL + String.format("?dateFrom=%s&dateTo=%s&returnRows=0",
                formattedBegin, formattedToday); // returnRows = 0 returns all transactions within those dates

        return getTransactions(url);
    }

    /**
     * Returns a list of transactions from a given date to now, containing only the last {@code quantity}
     * transactions.
     *
     * @param begin    starting date &amp; time
     * @param quantity number of transactions to display
     * @return List of the latest {@code quantity} WatTransaction from {@code begin} to now
     */
    public List<WatTransaction> getTransactions(LocalDateTime begin, int quantity) {
        String formattedBegin = WatTransaction.DATE_FORMAT.format(begin);
        String formattedToday = WatTransaction.DATE_FORMAT.format(LocalDateTime.now());
        String url = WatTransaction.BASE_URL + String.format("?dateFrom=%s&dateTo=%s&returnRows=%d",
                formattedBegin, formattedToday, quantity);

        return getTransactions(url);
    }

    /**
     * Returns a list of all transactions completed between two dates.
     *
     * @param begin starting date &amp; time
     * @param end   ending date &amp; time
     * @return List of all WatTransaction completed between {@code begin} and {@code end}
     */
    public List<WatTransaction> getTransactions(LocalDateTime begin, LocalDateTime end) {
        String formattedBegin = WatTransaction.DATE_FORMAT.format(begin);
        String formattedEnd = WatTransaction.DATE_FORMAT.format(end);
        String url = WatTransaction.BASE_URL + String.format("?dateFrom=%s&dateTo=%s&returnRows=0",
                formattedBegin, formattedEnd);

        return getTransactions(url);
    }

    /**
     * Returns a list of all transactions completed between two dates, containing only the last {@code quantity}
     * transactions.
     *
     * @param begin    starting date &amp; time
     * @param end      ending date &amp; time
     * @param quantity number of transactions to display
     * @return List of the latest {@code quantity} WatTransaction from {@code begin} to {@code end}
     */
    public List<WatTransaction> getTransactions(LocalDateTime begin, LocalDateTime end, int quantity) {
        String formattedBegin = WatTransaction.DATE_FORMAT.format(begin);
        String formattedEnd = WatTransaction.DATE_FORMAT.format(end);
        String url = WatTransaction.BASE_URL + String.format("?dateFrom=%s&dateTo=%s&returnRows=%d",
                formattedBegin, formattedEnd, quantity);

        return getTransactions(url);
    }

    /**
     * Returns a list of all transactions completed within a given amount of days from now.
     *
     * @param days  number of days
     * @param exact true, meaning only transactions made within last {@code begin} days (precise to the second) from now
     *              false, meaning transactions made from 0:00:00 of {@code begin} to now.
     * @return a list of WatTransactions completed within the last {@code begin} days
     */
    public List<WatTransaction> getLastDaysTransactions(int days, boolean exact) {
        String formattedBegin;
        String formattedEnd;

        if (exact) {
            formattedBegin = WatTransaction.DATE_FORMAT.format(LocalDateTime.now().minusDays(days));
            formattedEnd = WatTransaction.DATE_FORMAT.format(LocalDateTime.now());
        }
        else {
            formattedBegin = WatTransaction.DATE_FORMAT.format(LocalDateTime.now().minusDays(days).
                    truncatedTo(ChronoUnit.DAYS));
            formattedEnd = WatTransaction.DATE_FORMAT.format(LocalDateTime.now());
        }

        String url = WatTransaction.BASE_URL + String.format("?dateFrom=%s&dateTo=%s&returnRows=0",
                formattedBegin, formattedEnd);

        return getTransactions(url);
    }

    // Getters and setters
    public WatSession getSession() {
        return session;
    }

    public void setSession(WatSession session) {
        this.session = session;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public void setBalances(List<WatBalance> balances) {
        this.balances = balances;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}