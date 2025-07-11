package ru.netology;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class Response {
    private static final String HTTP_VERSION = "HTTP/1.1 ";
    private static final String CRLF = "\r\n";
    private static final String CONTENT_TYPE = "Content-Type: ";
    private static final String CONTENT_LENGTH = "Content-Length: ";
    private static final String CONNECTION_CLOSE = "Connection: close";
    private static final String DEFAULT_MIME_TYPE = "text/html";

    private final OutputStream out;

    public Response(OutputStream outputStream) {
        this.out = outputStream;
    }

    public void send(String statusCode, String statusMessage, String mimeType, byte[] body ) throws IOException {
        out.write((HTTP_VERSION + statusCode + " " + statusMessage + CRLF).getBytes(StandardCharsets.UTF_8));
        out.write((CONTENT_TYPE + mimeType + CRLF).getBytes(StandardCharsets.UTF_8));
        out.write((CONTENT_LENGTH + body.length + CRLF).getBytes(StandardCharsets.UTF_8));
        out.write((CONNECTION_CLOSE + CRLF + CRLF).getBytes(StandardCharsets.UTF_8));
        out.write(body);
        out.flush();
    }

    public void sendNotFound() throws IOException{
        send("404", "Not Found", DEFAULT_MIME_TYPE, "<h1>404 Not Found</h1>".getBytes(StandardCharsets.UTF_8));
    }

    public void sendBadRequest() throws IOException{
        send("400", "Bad Request", DEFAULT_MIME_TYPE, "<h1>400 Bad Request</h1>".getBytes(StandardCharsets.UTF_8));
    }
}
