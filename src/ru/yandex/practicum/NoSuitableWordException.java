package ru.yandex.practicum;

public class NoSuitableWordException extends RuntimeException {
    public NoSuitableWordException(String message) {
        super(message);
    }
}
