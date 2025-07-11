package ru.netology;

import java.io.*;

public class Main {
    public static void main(String[] args) throws IOException {

        Server server = new Server(8080);
        HandlerService.createHandlers(server);

        server.start();
    }
}


