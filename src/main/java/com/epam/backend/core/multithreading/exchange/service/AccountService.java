package com.epam.backend.core.multithreading.exchange.service;

import com.epam.backend.core.multithreading.exchange.dao.AccountDao;
import com.epam.backend.core.multithreading.exchange.dao.ExchangeRateDao;
import com.epam.backend.core.multithreading.exchange.model.Account;
import com.epam.backend.core.multithreading.exchange.model.Currency;
import com.epam.backend.core.multithreading.exchange.model.ExchangeRate;
import com.epam.backend.core.multithreading.exchange.service.exception.InsufficientFundsException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountService {
    private final AccountDao accountDao;
    private final ReentrantLock lock = new ReentrantLock();
    private static final Logger logger = Logger.getLogger(AccountService.class.getName());
    private final ExchangeRateDao exchangeRateDao;

    public AccountService(AccountDao accountDao, ExchangeRateDao exchangeRateDao) {
        this.accountDao = accountDao;
        this.exchangeRateDao = exchangeRateDao;
    }

    public void exchangeCurrency(String accountId, Currency from, Currency to, BigDecimal amount, ExchangeRate rate) throws InsufficientFundsException {
        lock.lock();
        try {
            var account = accountDao.loadAccount(accountId);
            var fromBalance = account.getBalances().getOrDefault(from, BigDecimal.ZERO);
            if (fromBalance.compareTo(amount) < 0) {
                throw new InsufficientFundsException("Insufficient funds for the transaction.");
            }
            var toAmount = rate.convert(amount);
            account.setBalance(from, fromBalance.subtract(amount));
            var toBalance = account.getBalances().getOrDefault(to, BigDecimal.ZERO);
            account.setBalance(to, toBalance.add(toAmount));
            accountDao.saveAccount(account);
            logger.info("Exchange completed: " + amount + " " + from.getCode() + " to " + to.getCode());
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Error occurred during the operation: "+ e.getMessage());
        } finally {
            lock.unlock();
        }
    }

    public void createAccount(String accountId, Map<Currency, BigDecimal> initialBalances) {
        Account newAccount = new Account(accountId, initialBalances);
        try {
            accountDao.saveAccount(newAccount);
            logger.info("Account created: " + accountId);
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to create account: " + e.getMessage(), e);
        }
    }

    public void updateAccountBalance(String accountId, Currency currency, BigDecimal newAmount) {
        lock.lock();
        try {
            var account = accountDao.loadAccount(accountId);
            account.setBalance(currency, newAmount);
            accountDao.saveAccount(account);
            logger.info("Account balance updated: Account ID = " + accountId + ", Currency = " + currency.getCode() + ", New Balance = " + newAmount);
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, "Failed to update account balance: " + e.getMessage(), e);
        } finally {
            lock.unlock();
        }
    }

    public void updateExchangeRate(Currency from, Currency to, BigDecimal newRate) {
        var newExchangeRate = new ExchangeRate(from, to, newRate);
        try {
            exchangeRateDao.saveExchangeRate(newExchangeRate);
            logger.info("Exchange rate updated from " + from.getCode() + " to " + to.getCode());
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Failed to update exchange rate: " + e.getMessage(), e);
        }
    }
}
