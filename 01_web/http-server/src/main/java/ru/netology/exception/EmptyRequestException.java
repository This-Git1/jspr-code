package ru.netology.exception;

import java.io.IOException;

public class EmptyRequestException extends IOException {
    public EmptyRequestException() {
        super("Empty request");
    }
}
