package com.epam.backend.core.multithreading.exchange;

import com.epam.backend.core.multithreading.exchange.dao.AccountDao;
import com.epam.backend.core.multithreading.exchange.model.Account;
import com.epam.backend.core.multithreading.exchange.model.Currency;
import com.epam.backend.core.multithreading.exchange.model.ExchangeRate;
import com.epam.backend.core.multithreading.exchange.service.AccountService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExchangeApp {

    public static final Currency USD = new Currency("USD");
    public static final Currency EUR = new Currency("EUR");

    public static void main(String[] args) {
        AccountDao accountDao = new AccountDao();
        AccountService accountService = new AccountService(accountDao);

        // Sample data
        Map<Currency, BigDecimal> balances = new HashMap<>();
        balances.put(USD, new BigDecimal("1000"));
        balances.put(EUR, new BigDecimal("850"));
        Account account = new Account("acc123", balances);
        ExchangeRate rate = new ExchangeRate(USD, EUR, new BigDecimal("0.85"));

        // Simulate concurrent access
        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> {
            try {
                accountService.exchangeCurrency("acc123", EUR, USD, new BigDecimal("100"), rate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        executor.submit(() -> {
            try {
                accountService.exchangeCurrency("acc123", USD, EUR, new BigDecimal("200"), rate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
