package ca.jeffrey.watcard;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;


public class WatSession {
    private CookieManager cookieManager;
    private HttpCookie verificationCookie;
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
            connection.getContent();

            HttpCookie verificationCookie = null;

            if (cookieManager.getCookieStore().getCookies().size() > 0) {
                List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();

                if (cookies != null) {
                    verificationCookie = cookies.get(0);
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
            setVerificationCookie(verificationCookie);
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public HttpCookie getVerificationCookie() {
        return verificationCookie;
    }

    public void setVerificationCookie(HttpCookie verification_cookie) {
        this.verificationCookie = verification_cookie;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verification_token) {
        this.verificationToken = verification_token;
    }

    public CookieManager getCookieManager() {
        return cookieManager;
    }

    public void setCookieManager(CookieManager cookieManager) {
        this.cookieManager = cookieManager;
    }
}
