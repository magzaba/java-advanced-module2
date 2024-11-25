package com.epam.backend.core.multithreading.exchange.service;

import com.epam.backend.core.multithreading.exchange.dao.AccountDao;
import com.epam.backend.core.multithreading.exchange.dao.ExchangeRateDao;
import com.epam.backend.core.multithreading.exchange.model.Account;
import com.epam.backend.core.multithreading.exchange.model.Currency;
import com.epam.backend.core.multithreading.exchange.model.ExchangeRate;
import com.epam.backend.core.multithreading.exchange.service.exception.InsufficientFundsException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertEquals;

public class AccountServiceTest {
    public static final Currency USD = new Currency("USD");
    public static final Currency EUR = new Currency("EUR");
    public static final String ACCOUNT_ID = "test135";
    private AccountService accountService;
    private AccountDao accountDaoMock;
    private ExchangeRateDao exchangeRateDaoMock;

    @BeforeMethod
    public void setUp() {
        accountDaoMock = mock(AccountDao.class);
        exchangeRateDaoMock = mock(ExchangeRateDao.class);
        accountService = new AccountService(accountDaoMock, exchangeRateDaoMock);
    }

    @Test
    public void testExchangeCurrencySuccess() throws Exception {
        Map<Currency, BigDecimal> balances = new HashMap<>();
        balances.put(USD, new BigDecimal("1000"));
        balances.put(EUR, new BigDecimal("500"));
        var account = new Account(ACCOUNT_ID, balances);
        var rate = new ExchangeRate(USD, EUR, new BigDecimal("0.85"));

        when(accountDaoMock.loadAccount(ACCOUNT_ID)).thenReturn(account);

        accountService.exchangeCurrency(ACCOUNT_ID, USD, EUR, new BigDecimal("100"), rate);

        verify(accountDaoMock).saveAccount(account);
        assertEquals(account.getBalances().get(USD), new BigDecimal("900.00"));
        assertEquals(account.getBalances().get(EUR), new BigDecimal("585.00"));
    }

    @Test(expectedExceptions = InsufficientFundsException.class)
    public void testExchangeCurrencyInsufficientFunds() throws Exception {
        Map<Currency, BigDecimal> balances = new HashMap<>();
        balances.put(USD, new BigDecimal("50"));
        var account = new Account(ACCOUNT_ID, balances);
        var rate = new ExchangeRate(USD, EUR, new BigDecimal("0.85"));

        when(accountDaoMock.loadAccount(ACCOUNT_ID)).thenReturn(account);

        accountService.exchangeCurrency(ACCOUNT_ID, USD, EUR, new BigDecimal("100"), rate);
    }

    @Test
    public void testCreateAccount() throws Exception {
        Map<Currency, BigDecimal> initialBalances = new HashMap<>();
        initialBalances.put(USD, new BigDecimal("5000"));

        doNothing().when(accountDaoMock).saveAccount(any(Account.class));

        accountService.createAccount(ACCOUNT_ID, initialBalances);

        verify(accountDaoMock).saveAccount(any(Account.class));
    }

    @Test(expectedExceptions = IOException.class)
    public void testCreateAccountIOException() throws Exception {
        Map<Currency, BigDecimal> initialBalances = new HashMap<>();
        initialBalances.put(USD, new BigDecimal("5000"));

        doThrow(new IOException("Failed to save account")).when(accountDaoMock).saveAccount(any(Account.class));

        accountService.createAccount(ACCOUNT_ID, initialBalances);
    }

    @Test
    public void testUpdateAccountBalance() throws Exception {
        Map<Currency, BigDecimal> initialBalances = new HashMap<>();
        initialBalances.put(USD, new BigDecimal("1000"));
        var account = new Account(ACCOUNT_ID, initialBalances);

        when(accountDaoMock.loadAccount(ACCOUNT_ID)).thenReturn(account);

        var newBalance = new BigDecimal("1200.00");
        accountService.updateAccountBalance(ACCOUNT_ID, USD, newBalance);

        verify(accountDaoMock).saveAccount(account);
        assertEquals(account.getBalances().get(USD), newBalance);
    }

    @Test(expectedExceptions = NoSuchElementException.class)
    public void testUpdateAccountBalanceWithNonExistingCurrency() throws Exception {
        Map<Currency, BigDecimal> initialBalances = new HashMap<>();
        initialBalances.put(USD, new BigDecimal("1000"));
        var account = new Account(ACCOUNT_ID, initialBalances);

        when(accountDaoMock.loadAccount(ACCOUNT_ID)).thenReturn(account);

        accountService.updateAccountBalance(ACCOUNT_ID, EUR, new BigDecimal("500.00"));
    }

    @Test
    public void testUpdateExchangeRate() throws Exception {

        var newRate = new BigDecimal("0.85");

        doNothing().when(exchangeRateDaoMock).saveExchangeRate(any(ExchangeRate.class));

        accountService.updateExchangeRate(USD, EUR, newRate);

        verify(exchangeRateDaoMock).saveExchangeRate(any(ExchangeRate.class));
    }

    @Test(expectedExceptions = IOException.class)
    public void testUpdateExchangeRateIOException() throws Exception {

        BigDecimal newRate = new BigDecimal("0.85");

        doThrow(new IOException("Failed to update exchange rate")).when(exchangeRateDaoMock).saveExchangeRate(any(ExchangeRate.class));

        accountService.updateExchangeRate(USD, EUR, newRate);
    }
}