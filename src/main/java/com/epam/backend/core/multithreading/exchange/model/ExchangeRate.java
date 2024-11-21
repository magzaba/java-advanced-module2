package com.epam.backend.core.multithreading.exchange.model;


import java.io.Serializable;
import java.math.BigDecimal;


public class ExchangeRate  implements Serializable {
    private final Currency from;
    private final Currency to;
    private final BigDecimal rate;

    public ExchangeRate(Currency from, Currency to, BigDecimal rate) {
        this.from = from;
        this.to = to;
        this.rate = rate;
    }

    public BigDecimal convert(BigDecimal amount) {
        return amount.multiply(rate);
    }
}
