package ca.jeffrey.watcard;

import org.apache.http.cookie.Cookie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;


public class WatSession {
    private CookieStore cookieStore;
    private CookieManager cookieManager;
    private Cookie verificationCookie;
    private String verificationToken;

    public WatSession() {
        initializeSession();
    }


    private void initializeSession() {
        final String LOGIN_URL = "https://watcard.uwaterloo.ca/OneWeb/Account/LogOn";

        // cookieStore = new BasicCookieStore();
        cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        try {
            URL url = new URL(LOGIN_URL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.connect();
            CookieStore cookieStore = cookieManager.getCookieStore();
            List cookieList = cookieStore.getCookies();
            System.out.println("Length: " + cookieList.size());
            if (cookieManager.getCookieStore().getCookies().size() > 0) {
                List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();

                if (cookies != null) {
                    for (HttpCookie cookie : cookies) {
                        // connection.setRequestProperty("Cookie", cookie.getName() + "=" + cookie.getValue());
                        System.err.println("Name: " + cookie.getName());
                        System.err.println("Value: " + cookie.getValue());
                    }
                }
            }

            InputStream inputStream = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            StringBuffer buffer = new StringBuffer("");
            while ((line = rd.readLine()) != null) {
                buffer.append(line);
            }

            String htmlResponse = buffer.toString();
            Document doc = Jsoup.parse(htmlResponse);

            String requestVerificationToken = doc.select("input[name=__RequestVerificationToken]").get(0).val();
            setVerificationToken(requestVerificationToken);
            // setVerificationCookie(cookieStore.getCookies().get(0));
            System.out.println(requestVerificationToken);
            connection.disconnect();
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public Cookie getVerificationCookie() {
        return verificationCookie;
    }

    public void setVerificationCookie(Cookie verification_cookie) {
        this.verificationCookie = verification_cookie;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verification_token) {
        this.verificationToken = verification_token;
    }

    public CookieStore getCookieStore() {
        return cookieStore;
    }

    public void setCookieStore(CookieStore cookie_store) {
        this.cookieStore = cookie_store;
    }
}
