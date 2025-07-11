package ru.netology;

import ru.netology.exception.BadRequestException;
import ru.netology.exception.EmptyRequestException;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.net.URI;

public class RequestParser {

    public static Request parse(InputStream inputStream) throws IOException {
        BufferedInputStream bufferedInput = new BufferedInputStream(inputStream);
        bufferedInput.mark(8192);

        BufferedReader reader = new BufferedReader(new InputStreamReader(bufferedInput, StandardCharsets.UTF_8));

        String requestLine = readLine(reader);
        String[] requestLineParts = parseRequestLine(requestLine);

        String method = requestLineParts[0];
        URI uri = parseUri(requestLineParts[1]);


        Map<String, String> headers = readHeaders(reader);
        int contentLength = getContentLength(headers);

        if (method.equals("GET") || method.equals("DELETE")) {
            return new Request(method, uri, headers, new byte[0]);
        }

        byte[] body = readBody(bufferedInput, contentLength);

        return new Request(method, uri, headers, body);
    }

    private static String readLine(BufferedReader reader) throws IOException {
        String requestLine = reader.readLine();
        if (requestLine == null || requestLine.isEmpty()) {
            throw new EmptyRequestException();
        }
        return requestLine;
    }

    private static String[] parseRequestLine(String requestLine) throws BadRequestException {
        String[] parts = requestLine.split(" ");
        if (parts.length != 3) {
            throw new BadRequestException("Invalid request line: " + requestLine);
        }
        return parts;
    }

    private static URI parseUri(String rawUri) throws BadRequestException {
        try {
            return new URI(rawUri);
        } catch (URISyntaxException e) {
            throw new BadRequestException("Invalid URI: " + rawUri);
        }
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
                throw new BadRequestException("Invalid Content-Length header");
            }
        }
        return contentLength;
    }

    private static byte[] readBody(BufferedInputStream bufferedInputStream, int contentLength) throws IOException {
        System.out.println("Читаем тело запроса длинной: " + contentLength);
        bufferedInputStream.reset();
        if (contentLength <= 0) {
            return new byte[0];
        }

        byte[] body = new byte[contentLength];
        int bytesReadTotal = 0;
        while (bytesReadTotal < contentLength) {
            int read = bufferedInputStream.read(body, bytesReadTotal, contentLength - bytesReadTotal);
            if (read == -1) {
                throw new IOException("Unexpected end of stream while reading body");
            }
            bytesReadTotal += read;
        }
        return body;
    }
}
