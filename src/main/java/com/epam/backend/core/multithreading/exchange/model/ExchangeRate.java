package com.epam.backend.core.multithreading.exchange.model;


import java.io.Serializable;
import java.math.BigDecimal;


public class ExchangeRate  implements Serializable {

    private static final long serialVersionUID = 3456789L;
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

    public Currency getFrom() {
        return from;
    }

    public Currency getTo() {
        return to;
    }

    public BigDecimal getRate() {
        return rate;
    }
}
