package ru.netology;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;

public class HandlerService {
    private static final List<String> VALID_PATHS = List.of(
            "/index.html", "/spring.svg", "/spring.png", "/resources.html", "/styles.css",
            "/app.js", "/links.html", "/forms.html", "/classic.html", "/events.html", "/events.js"
    );

    public static void createHandlers(HttpServer server) {
        for (String path : VALID_PATHS) {
            registerHandler(path, server);
        }
    }

    private static void registerHandler(String path, HttpServer server) {
        Path filePath = Path.of("public", path);
        if (!Files.exists(filePath)) {
            System.err.println("⚠️  File not found: " + filePath.toAbsolutePath());
            return;
        }

        String mimeType = resolveMimeType(filePath);

        if ("/classic.html".equals(path)) {
            registerDynamicHandler(server, path, filePath, mimeType);
        } else {
            registerStaticHandler(server, path, filePath, mimeType);
        }
    }

    private static void registerStaticHandler(HttpServer server, String path, Path filePath, String mimeType) {
        server.addHandler("GET", path, (req, res) -> {
            try {
                byte[] content = Files.readAllBytes(filePath);
                res.send("200", "OK", mimeType, content);
            } catch (IOException e) {
                res.sendNotFound();
            }
        });
        System.out.println("✅ Registered static GET handler for " + path);
    }

    private static void registerDynamicHandler(HttpServer server, String path, Path filePath, String mimeType) {
        server.addHandler("GET", path, (req, res) -> {
            try {
                String template = Files.readString(filePath);
                String processed = template.replace("{time}", LocalDateTime.now().toString());
                res.send("200", "OK", mimeType, processed.getBytes());
            } catch (IOException e) {
                res.sendNotFound();
            }
        });
        System.out.println("✅ Registered dynamic GET handler for " + path);
    }

    private static String resolveMimeType(Path path) {
        try {
            String type = Files.probeContentType(path);
            return type != null ? type : "application/octet-stream";
        } catch (IOException e) {
            return "application/octet-stream";
        }
    }
}
