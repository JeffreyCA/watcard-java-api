package ca.jeffrey.watcard.api;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;


public class WatSession {
    private CookieStore cookieStore;
    private Cookie verificationCookie;
    private String verificationToken;

    public WatSession() {
        initializeSession();
    }


    private void initializeSession() {
        final String LOGIN_URL = "https://watcard.uwaterloo.ca/OneWeb/Account/LogOn";

        cookieStore = new BasicCookieStore();
        HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(cookieStore).build();
        HttpGet get = new HttpGet(LOGIN_URL);

        try {
            HttpResponse response = client.execute(get);
            String htmlResponse = new BasicResponseHandler().handleResponse(response);
            Document doc = Jsoup.parse(htmlResponse);

            String requestVerificationToken = doc.select("input[name=__RequestVerificationToken]").get(0).val();
            setVerificationToken(requestVerificationToken);
            // Should only be one cookie in request
            setVerificationCookie(cookieStore.getCookies().get(0));
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
