package com.epam.backend.core.multithreading.exchange.model;

import java.io.Serializable;

public class Currency implements Serializable {

    private final String code;

    public Currency(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
