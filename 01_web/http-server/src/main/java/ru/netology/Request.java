package ru.netology;

import java.util.Map;

public class Request {
    private final String method;
    private final String url;
    private final Map<String, String> headers;
    private final byte[] body;

    public Request(String method, String url, Map<String, String> headers, byte[] body) {
        this.method = method;
        this.url = url;
        this.headers = headers;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }
}
