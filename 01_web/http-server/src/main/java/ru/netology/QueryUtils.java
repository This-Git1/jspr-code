package ru.netology;

import org.apache.hc.core5.http.NameValuePair;
import org.apache.hc.core5.net.URLEncodedUtils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryUtils {

    private QueryUtils() {
    }

    public static Map<String, List<String>> parseQueryParams(URI uri) {
        if (uri == null || uri.getQuery() == null || uri.getQuery().isBlank()) {
            return Map.of();
        }

        String query = uri.getRawQuery();

        List<NameValuePair> params = URLEncodedUtils.parse(query, StandardCharsets.UTF_8);
        Map<String, List<String>> queryParams = new HashMap<>();

        for (NameValuePair param : params) {
            String name = param.getName();
            String value = param.getValue();

            // добавляем значение к существующему ключу или создаем новую запись
            queryParams.computeIfAbsent(name, k -> new ArrayList<>()).add(value);
        }
        return queryParams;
    }
}
