package br.com.bernardorufino.android.universitario.ext.rest;


import com.google.common.base.Throwables;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class RestClient {

    private static final String CHARSET = "UTF-8";

    public static String encode(String string) {
        try {
            return URLEncoder.encode(string, CHARSET);
        } catch (UnsupportedEncodingException e) {
            return string;
        }
    }

    private volatile boolean mIsRequestOpen = false;
    private final String mHost;

    public RestClient(String hostUrl) {
        mHost = hostUrl.replaceFirst("/$", "");
    }

    public RestClient() {
        mHost = null;
    }

    private URL getUrl(String url) {
        if (mHost != null) {
            url = mHost + "/" + url.replaceFirst("^/", "");
        }
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            throw Throwables.propagate(e);
        }
    }

    public boolean isRequestOpen() {
        // Only read, don't need to synchronize
        return mIsRequestOpen;
    }

    public String request(HttpURLConnection connection) throws IOException {
        connection.setDoInput(true);
        mIsRequestOpen = true;
        connection.connect();
        mIsRequestOpen = false;
        if (connection.getResponseCode() != 200) return null;
        return IOUtils.toString(connection.getInputStream());
    }

    public String get(String url) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) getUrl(url).openConnection();
        connection.setRequestMethod("GET");
        String response = request(connection);
        return response;
    }

    public JSONObject getJson(String url) throws IOException {
        try {
            return new JSONObject(get(url));
        } catch (JSONException e) {
            throw Throwables.propagate(e);
        }
    }
}
