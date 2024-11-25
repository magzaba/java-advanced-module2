package com.epam.backend.core.multithreading.exchange.dao;

import com.epam.backend.core.multithreading.exchange.model.Currency;
import com.epam.backend.core.multithreading.exchange.model.ExchangeRate;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.Assert;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;
import static org.mockito.ArgumentMatchers.any;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigDecimal;

public class ExchangeRateDaoTest {

    @Mock
    private ObjectOutputStream mockObjectOutputStream;
    @Mock
    private ObjectInputStream mockObjectInputStream;

    private ExchangeRateDao exchangeRateDao;
    private final Currency fromCurrency = new Currency("USD");
    private final Currency toCurrency = new Currency("EUR");
    private final BigDecimal rate = new BigDecimal("0.85");
    private final ExchangeRate exchangeRate = new ExchangeRate(fromCurrency, toCurrency, rate);

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);

        whenNew(ObjectOutputStream.class).withAnyArguments().thenReturn(mockObjectOutputStream);
        whenNew(ObjectInputStream.class).withAnyArguments().thenReturn(mockObjectInputStream);
        when(mockObjectInputStream.readObject()).thenReturn(exchangeRate);

        exchangeRateDao = new ExchangeRateDao();
    }

    @Test
    public void testSaveExchangeRate() throws IOException {
        exchangeRateDao.saveExchangeRate(exchangeRate);
        verify(mockObjectOutputStream).writeObject(any(ExchangeRate.class));
    }

    @Test
    public void testLoadExchangeRate() throws IOException, ClassNotFoundException {
        var loadedExchangeRate = exchangeRateDao.loadExchangeRate(fromCurrency.getCode(), toCurrency.getCode());
        verify(mockObjectInputStream).readObject();
        Assert.assertEquals(exchangeRate.getFrom().getCode(), loadedExchangeRate.getFrom().getCode());
        Assert.assertEquals(exchangeRate.getTo().getCode(), loadedExchangeRate.getTo().getCode());
        Assert.assertEquals(rate.compareTo(loadedExchangeRate.getRate()), 0);
    }

    @Test(expectedExceptions = IOException.class)
    public void testIOExceptionOnSave() throws IOException {
        doThrow(IOException.class).when(mockObjectOutputStream).writeObject(any());
        exchangeRateDao.saveExchangeRate(exchangeRate);
    }

    @Test(expectedExceptions = IOException.class)
    public void testIOExceptionOnLoad() throws IOException, ClassNotFoundException {
        when(mockObjectInputStream.readObject()).thenThrow(IOException.class);
        exchangeRateDao.loadExchangeRate(fromCurrency.getCode(), toCurrency.getCode());
    }
}