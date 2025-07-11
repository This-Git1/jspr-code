package ru.netology;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestParser {

    public static Request parse(InputStream inputStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        String requestLine = readLine(in);
        String[] requestLineParts = parseRequestLine(requestLine);

        String method = requestLineParts[0];
        String path = requestLineParts[1];

        Map<String, String> headers = readHeaders(in);
        int contentLength = getContentLength(headers);
        byte[] body = readBody(inputStream, contentLength);

        return new Request(method, path, headers, body);
    }

    private static String readLine(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            throw new EmptyRequestException();
        }
        return requestLine;
    }

    private static String[] parseRequestLine(String requestLine) throws IOException{
        String[] parts = requestLine.split(" ");
        if (parts.length != 3) {
            throw new BadRequestException();
        }
        return parts;
    }

    private static Map<String, String> readHeaders(BufferedReader reader) throws IOException {
        Map<String, String> headers = new HashMap<>();
        String line;
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            String[] headerParts = line.split(":\\s*", 2);
            if (headerParts.length == 2) headers.put(headerParts[0], headerParts[1]);
        }
        return headers;
    }

    private static int getContentLength(Map<String, String> headers) throws IOException {
        int contentLength = 0;
        if (headers.containsKey("Content-Length")) {
            try {
                contentLength = Integer.parseInt(headers.get("Content-Length"));
            } catch (NumberFormatException e) {
                throw new IOException("Invalid Content-Length header");
            }
        }
        return contentLength;
    }

    private static byte[] readBody(InputStream inputStream, int contentLength) throws IOException {
        byte[] body = new byte[contentLength];
        if (contentLength > 0) {
           int bytesRead = inputStream.read(body);
           if (bytesRead != contentLength) {
               throw new IOException("Incomplete body read");
           }
        }
        return body;
    }
}
