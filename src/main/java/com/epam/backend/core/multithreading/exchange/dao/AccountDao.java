package com.epam.backend.core.multithreading.exchange.dao;

import com.epam.backend.core.multithreading.exchange.model.Account;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class AccountDao {
    private static final String ACCOUNTS_DIR = "accounts/";

    public Account loadAccount(String accountId) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ACCOUNTS_DIR + accountId + ".dat"))) {
            return (Account) ois.readObject();
        }
    }

    public void saveAccount(Account account) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ACCOUNTS_DIR + account.getAccountId() + ".dat"))) {
            oos.writeObject(account);
        }
    }

}