package com.epam.backend.core.multithreading.exchange.service.exception;

public class InsufficientFundsException extends Exception {

    public InsufficientFundsException(String message) {
            super(message);
    }
}
