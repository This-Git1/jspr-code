package ru.netology;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URLEncodedUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class QueryUtils {

    public static Map<String, String> parseQueryParams(String url) throws RuntimeException {
        try {
            URI uri = new URI(url);
            List<NameValuePair> params = URLEncodedUtils.parse(uri, StandardCharsets.UTF_8);

            Map<String, String> queryParams = new HashMap<>();
            for (NameValuePair param : params) {
                queryParams.put(param.getName(), param.getValue());
            }
            return queryParams;
        } catch (URISyntaxException e) {
            throw new RuntimeException("Invalid URL: " + url, e);
        }
    }
}
