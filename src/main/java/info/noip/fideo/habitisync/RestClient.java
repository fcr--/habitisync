package info.noip.fideo.habitisync;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author fran
 */
public class RestClient {

    private static String urlEncode(String param) {
        try {
            return URLEncoder.encode(param, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static JSONObject request(String url, String method, Map<String, String> headers, Map<String, String> urlParams, Map<String, String> bodyParams) throws MalformedURLException, IOException, HttpResultException, ParseException {
        if (!urlParams.isEmpty()) {
            String encodedParams = urlParams.entrySet().stream()
                    .map(e -> urlEncode(e.getKey()) + "=" + urlEncode(e.getValue()))
                    .collect(Collectors.joining("="));
            url += (url.contains("?") ? "&" : "?") + encodedParams;
        }
        HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
        connection.setUseCaches(false);
        connection.setRequestMethod(method);
        for (Map.Entry<String, String> header : headers.entrySet()) {
            connection.setRequestProperty(header.getKey(), header.getValue());
        }
        if (!bodyParams.isEmpty()) {
            String encodedParams = bodyParams.entrySet().stream()
                    .map(e -> urlEncode(e.getKey()) + "=" + urlEncode(e.getValue()))
                    .collect(Collectors.joining("="));
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=utf-8");
            connection.setDoOutput(true);
            connection.getOutputStream().write(encodedParams.getBytes());
        }
        String body = new BufferedReader(new InputStreamReader(connection.getInputStream()))
                .lines().collect(Collectors.joining("\n"));
        if (connection.getResponseCode() / 100 != 2) {
            throw new HttpResultException("Invalid status code", url, connection.getResponseCode());
        }
        return (JSONObject) new JSONParser().parse(body);
    }

    private static class HttpResultException extends Exception {

        public HttpResultException(String text, String url, int responseCode) {
            super(text + ": " + responseCode + "(" + url + ")");
        }
    }

    public static class Builder {

        private final String url;
        private String method = "GET";
        private final Map<String, String> headers = new HashMap<>(), urlParams = new HashMap<>(), bodyParams = new HashMap<>();

        public Builder(String url) {
            this.url = url;
        }

        public Builder method(String method) {
            this.method = method;
            return this;
        }

        public Builder header(String key, String value) {
            this.headers.put(key, value);
            return this;
        }

        public Builder urlParam(String key, String value) {
            this.urlParams.put(key, value);
            return this;
        }

        public Builder bodyParam(String key, String value) {
            this.bodyParams.put(key, value);
            return this;
        }

        public JSONObject request() throws IOException, MalformedURLException, HttpResultException, ParseException {
            return RestClient.request(url, method, headers, urlParams, bodyParams);
        }
    }
}
