package com.epam.backend.core.multithreading.exchange.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;

public class Account implements Serializable {

    private static final long serialVersionUID = 1234567L;
    private final String accountId;
    private final Map<Currency, BigDecimal> balances;

    public Account(String accountId, Map<Currency, BigDecimal> balances) {
        this.accountId = accountId;
        this.balances = balances;
    }

    public String getAccountId() {
        return accountId;
    }

    public Map<Currency, BigDecimal> getBalances() {
        return balances;
    }

    public void setBalance(Currency currency, BigDecimal amount) {
        balances.put(currency, amount.setScale(2, RoundingMode.CEILING));
    }
}
