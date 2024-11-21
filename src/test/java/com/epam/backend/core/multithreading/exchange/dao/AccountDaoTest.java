package com.epam.backend.core.multithreading.exchange.dao;

import com.epam.backend.core.multithreading.exchange.model.Account;
import com.epam.backend.core.multithreading.exchange.model.Currency;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


public class AccountDaoTest {
    private AccountDao accountDao;
    private final String testAccountId = "testAccount";
    private final File testAccountFile = new File("accounts/" + testAccountId + ".dat");

    @BeforeMethod
    public void setUp() throws IOException {
        accountDao = new AccountDao();
        // Ensure the directory exists
        new File("accounts/").mkdirs();
        // Ensure we start with a clean state
        testAccountFile.delete();
    }

    @AfterMethod
    public void tearDown() {
        // Clean up after test
        testAccountFile.delete();
    }

    @Test
    public void testSaveAndLoadAccount() throws IOException, ClassNotFoundException {
        Map<Currency, BigDecimal> balances = new HashMap<>();
        balances.put(new Currency("USD"), new BigDecimal("1000"));
        var account = new Account(testAccountId, balances);

        accountDao.saveAccount(account);
        var loadedAccount = accountDao.loadAccount(testAccountId);

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertNotNull(loadedAccount);
        softAssert.assertEquals(loadedAccount.getAccountId(), testAccountId);
        softAssert.assertEquals(loadedAccount.getBalances().get(new Currency("USD")), new BigDecimal("1000"));
        softAssert.assertAll();
    }
}