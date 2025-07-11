package ru.netology;

import java.io.*;

public class Main {
  public static void main(String[] args) throws IOException {

    HttpServer server = new HttpServer(9999);
    HandlerService.createHandlers(server);

    server.start();
  }

}


