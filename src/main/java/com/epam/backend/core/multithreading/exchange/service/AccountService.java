package com.epam.backend.core.multithreading.exchange.service;

import com.epam.backend.core.multithreading.exchange.dao.AccountDao;
import com.epam.backend.core.multithreading.exchange.model.Currency;
import com.epam.backend.core.multithreading.exchange.model.ExchangeRate;
import com.epam.backend.core.multithreading.exchange.service.exception.InsufficientFundsException;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AccountService {
    private final AccountDao accountDao;
    private final ReentrantLock lock = new ReentrantLock();
    private static final Logger logger = Logger.getLogger(AccountService.class.getName());

    public AccountService(AccountDao accountDao) {
        this.accountDao = accountDao;
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
}
