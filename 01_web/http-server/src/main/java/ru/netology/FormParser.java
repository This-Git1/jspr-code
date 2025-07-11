package ru.netology;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.HashMap;
import java.net.URLDecoder;

public class FormParser {

    private FormParser() {
    }

    public static Map<String, String> parseFormData(byte[] body) {
        Map<String, String> formData = new HashMap<>();
        String content = new String(body, StandardCharsets.UTF_8);

        String[] params = content.split("&");

        for (String param : params) {
            String[] keyValue = param.split("=");

            if (keyValue.length >= 1) {
                String key = urlDecode(keyValue[0]);
                String value = keyValue.length == 2 ? urlDecode(keyValue[1]) : "";

                formData.put(key, value);
            }
        }
        return formData;
    }

    private static String urlDecode(String value) {
        return URLDecoder.decode(value, StandardCharsets.UTF_8);
    }
}
