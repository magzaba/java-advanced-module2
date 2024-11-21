package com.epam.backend.core.multithreading.exchange.service;

import com.epam.backend.core.multithreading.exchange.dao.AccountDao;
import com.epam.backend.core.multithreading.exchange.model.Account;
import com.epam.backend.core.multithreading.exchange.model.Currency;
import com.epam.backend.core.multithreading.exchange.model.ExchangeRate;
import com.epam.backend.core.multithreading.exchange.service.exception.InsufficientFundsException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

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

    @BeforeMethod
    public void setUp() {
        accountDaoMock = mock(AccountDao.class);
        accountService = new AccountService(accountDaoMock);
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
}