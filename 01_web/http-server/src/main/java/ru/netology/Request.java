package ru.netology;

import java.net.URI;
import java.util.Map;

public class Request {
    private final String method;
    private final URI uri;
    private final Map<String, String> headers;
    private final byte[] body;

    public Request(String method, URI uri, Map<String, String> headers, byte[] body) {
        this.method = method;
        this.uri = uri;
        this.headers = headers;
        this.body = body;
    }

    public String getMethod() {
        return method;
    }

    public URI getUri() {
        return uri;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }
}
