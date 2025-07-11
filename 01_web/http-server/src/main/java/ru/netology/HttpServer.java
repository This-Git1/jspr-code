package ru.netology;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {
    private final int port;
    private final ExecutorService executor;
    private final Map<String, Map<String, Handler>> handlers = new ConcurrentHashMap<>();

    public HttpServer(int port) {
        this.port = port;
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }

    public void addHandler(String method, String path, Handler handler) {
        String methodUpperCase = method.toUpperCase();
        handlers
                .computeIfAbsent(methodUpperCase, k -> new ConcurrentHashMap<>())
                .put(path, handler);
    }

    public void start() {
        try (final var serverSocket = new ServerSocket(port)) {
            System.out.println("Server started on port " + port);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                executor.submit(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            throw new RuntimeException("Server failed to start", e);
        }
    }

    private void handleClient(Socket clientSocket) {
        System.out.println("New client connection from " + clientSocket.getRemoteSocketAddress());
        try (
                clientSocket;
                InputStream in = clientSocket.getInputStream();
                OutputStream out = new BufferedOutputStream(clientSocket.getOutputStream())
                ) {

            Response response = new Response(out);
            try {
                Request request = RequestParser.parse(in);
                dispatchRequest(request, response);
            } catch (EmptyRequestException e) {
                response.sendNotFound();
            } catch (BadRequestException e) {
                response.sendBadRequest();
            } catch (IOException e) {
                System.err.println("Failed to parse request: " + e.getMessage());
                response.sendBadRequest();
            }

        } catch (IOException e) {
            System.err.println("Failed to parse request: " + e.getMessage());
        }
    }

    private void dispatchRequest(Request request, Response response) throws IOException {
        var methodHandlers = handlers.get(request.getMethod());
        if (methodHandlers == null) {
            response.sendNotFound();
            return;
        }

        var handler = methodHandlers.get(request.getUrl());
        if (handler == null) {
            response.sendNotFound();
            return;
        }

        handler.handle(request, response);
    }


}
