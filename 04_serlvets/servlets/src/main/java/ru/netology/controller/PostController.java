package ru.netology.controller;

import com.google.gson.Gson;
import org.springframework.stereotype.Controller;
import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import ru.netology.service.PostService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

@Controller
public class PostController {
    public static final String APPLICATION_JSON = "application/json";
    private final PostService service;

    public PostController(PostService service) {
        this.service = service;
    }

    public void all(HttpServletResponse response) throws IOException {
        writeResponseForJson(response, HttpServletResponse.SC_OK, service.all());
    }

    public void getById(long id, HttpServletResponse response) throws IOException {
        writeResponseForJson(response, HttpServletResponse.SC_OK, service.getById(id));
    }

    public void save(Reader body, HttpServletResponse response) throws IOException {
        try {
            final var gson = new Gson();
            final var post = gson.fromJson(body, Post.class);
            final var data = service.save(post);
            writeResponseForJson(response, HttpServletResponse.SC_CREATED, data);
        } catch (NotFoundException e) {
            writeResponseForJson(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    public void removeById(long id, HttpServletResponse response) throws IOException {
        try {
            service.removeById(id);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } catch (NotFoundException e) {
            writeResponseForJson(response, HttpServletResponse.SC_NOT_FOUND, Map.of("Error", e.getMessage()));
        }
    }

    private void writeResponseForJson(HttpServletResponse response, int status, Object data) throws IOException {
        response.setStatus(status);
        response.setContentType(APPLICATION_JSON);
        response.getWriter().print(new Gson().toJson(data));
    }
}
