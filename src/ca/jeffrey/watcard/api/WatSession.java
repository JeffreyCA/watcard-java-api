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
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class WatSession {
    private CookieStore cookie_store;
    private Cookie verification_cookie;
    private String verification_token;

    public WatSession() {
        initializeSession();
    }


    private void initializeSession() {
        final String LOGIN_URL = "https://watcard.uwaterloo.ca/OneWeb/Account/LogOn";

        cookie_store = new BasicCookieStore();
        HttpClient client = HttpClientBuilder.create().setDefaultCookieStore(cookie_store).build();
        HttpGet get = new HttpGet(LOGIN_URL);

        try {
            HttpResponse response = client.execute(get);
            String html_response = new BasicResponseHandler().handleResponse(response);
            Document doc = Jsoup.parse(html_response);

            String requestVerificationToken = doc.select("input[name=__RequestVerificationToken]").get(0).val();
            setVerificationToken(requestVerificationToken);
            // Should only be one cookie in request
            setVerificationCookie(cookie_store.getCookies().get(0));
        }
        catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    public Cookie getVerificationCookie() {
        return verification_cookie;
    }
    public void setVerificationCookie(Cookie verification_cookie) {
        this.verification_cookie = verification_cookie;
    }
    public String getVerificationToken() {
        return verification_token;
    }
    public void setVerificationToken(String verification_token) {
        this.verification_token = verification_token;
    }
    public CookieStore getCookieStore() {
        return cookie_store;
    }
    public void setCookieStore(CookieStore cookie_store) {
        this.cookie_store = cookie_store;
    }
}
