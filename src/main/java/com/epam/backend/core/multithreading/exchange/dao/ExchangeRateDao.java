package com.epam.backend.core.multithreading.exchange.dao;

import com.epam.backend.core.multithreading.exchange.model.ExchangeRate;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ExchangeRateDao {
    private static final String EXCHANGE_RATES_DIR = "exchangeRates/";

    public ExchangeRate loadExchangeRate(String fromCurrencyCode, String toCurrencyCode) throws IOException, ClassNotFoundException {
        String filename = fromCurrencyCode + "_to_" + toCurrencyCode + ".dat";
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(EXCHANGE_RATES_DIR + filename))) {
            return (ExchangeRate) ois.readObject();
        }
    }

    public void saveExchangeRate(ExchangeRate exchangeRate) throws IOException {
        var filename = exchangeRate.getFrom().getCode() + "_to_" + exchangeRate.getTo().getCode() + ".dat";
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(EXCHANGE_RATES_DIR + filename))) {
            oos.writeObject(exchangeRate);
        }
    }
}
