package ca.jeffrey.watcard;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;


public class WatSession {

    private CookieManager cookieManager;
    private HttpCookie verificationCookie;
    private String verificationToken;

    public WatSession() {
        initializeSession();
    }


    private void initializeSession() {
        final String LOGIN_URL = "https://watcard.uwaterloo.ca/OneWeb/Account/LogOn";

        cookieManager = new CookieManager();
        CookieHandler.setDefault(cookieManager);
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        try {
            URL url = new URL(LOGIN_URL);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.getContent();

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
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }
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
}
