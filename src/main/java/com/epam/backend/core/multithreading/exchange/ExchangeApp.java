package com.epam.backend.core.multithreading.exchange;

import com.epam.backend.core.multithreading.exchange.dao.AccountDao;
import com.epam.backend.core.multithreading.exchange.dao.ExchangeRateDao;
import com.epam.backend.core.multithreading.exchange.model.Currency;
import com.epam.backend.core.multithreading.exchange.model.ExchangeRate;
import com.epam.backend.core.multithreading.exchange.service.AccountService;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ExchangeApp {

    private static final Logger logger = Logger.getLogger(ExchangeApp.class.getName());
    public static final Currency USD = new Currency("USD");
    public static final Currency EUR = new Currency("EUR");

    public static void main(String[] args) {
        AccountDao accountDao = new AccountDao();
        ExchangeRateDao exchangeRateDao = new ExchangeRateDao();
        AccountService accountService = new AccountService(accountDao, exchangeRateDao);

        // Sample data
        String accountId = "acc123";
        Map<Currency, BigDecimal> initialBalances = new HashMap<>();
        initialBalances.put(USD, new BigDecimal("1000"));
        initialBalances.put(EUR, new BigDecimal("850"));


        // 1. Create an account
        accountService.createAccount(accountId, initialBalances);

        // 2. Update an account balance
        accountService.updateAccountBalance(accountId, USD, new BigDecimal("1200"));

        // 3. Update exchange rates
        var newRate = new ExchangeRate(USD, EUR, new BigDecimal("0.90"));
        accountService.updateExchangeRate(USD, EUR, new BigDecimal("0.90"));

        // Simulate concurrent currency exchange access
        var executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> {
            try {
                // 4. Exchange EUR to USD
                accountService.exchangeCurrency(accountId, EUR, USD, new BigDecimal("100"), newRate);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error during EUR to USD exchange: " + e.getMessage(), e);
            }
        });
        executor.submit(() -> {
            try {
                // 4. Exchange USD to EUR
                accountService.exchangeCurrency(accountId, USD, EUR, new BigDecimal("200"), newRate);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error during USD to EUR exchange: " + e.getMessage(), e);
            }
        });

        // Shut down the executor service
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            logger.log(Level.SEVERE, "Error while waiting for termination: " + e.getMessage(), e);
        }
    }
}
