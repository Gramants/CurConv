package ste.demo.converter.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModel;

import ste.demo.converter.api.ApiRatesClient;
import ste.demo.converter.api.model.LatestRates;
import ste.demo.converter.viewmodel.model.CurrencyRate;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;


public class RatesViewModel extends ViewModel {

    private String baseCurrency = "EUR";
    private final MutableLiveData currencyRates = new MutableLiveData();
    private float baseValue = 100.0F;
    private ApiRatesClient apiRatesClient;

    private final Observer latestRatesObs = new Observer() {
        @Override
        public void onChanged(Object observable) {
            if ((observable != null) && (observable instanceof LatestRates)) {
                updateLatestRates((LatestRates) observable);
            }
        }

    };


    public RatesViewModel() {
        apiRatesClient = new ApiRatesClient();
        apiRatesClient.start();
        apiRatesClient.getLatestUpdateRates().observeForever(latestRatesObs);
    }


    private void updateLatestRates(LatestRates latestRates) {

        ArrayList<CurrencyRate> newCurrencyRates = new ArrayList();

        newCurrencyRates.add(new CurrencyRate(latestRates.getBase(), 1.0F, baseValue));
        for (Map.Entry<String, Float> entry : latestRates.getRates().entrySet()) {
            String currency = entry.getKey();
            Float rate = entry.getValue();
            newCurrencyRates.add(new CurrencyRate(currency, rate, rate * baseValue));
        }

        currencyRates.setValue(newCurrencyRates);

    }


    public final LiveData getCurrencyRates() {
        return currencyRates;
    }

    public final void refreshRates() {
        apiRatesClient.refreshLatestUpdateRates(this.baseCurrency);
    }

    public final void setNewBase(String newBaseCurrency, float newBaseValue) {

        if (!this.baseCurrency.equals(newBaseCurrency)) {
            this.baseCurrency = newBaseCurrency;
            this.baseValue = newBaseValue;
            this.refreshRates();
        }
    }


    public final void setNewBaseValue(float value) {
        if (Float.compare(baseValue, value) != 0) {

            this.baseValue = value;
            ArrayList newCurrencyRates = new ArrayList();
            if (currencyRates.getValue() != null) {
                Iterable iterable = (Iterable) currencyRates.getValue();
                Iterator iterator = iterable.iterator();
                while (iterator.hasNext()) {
                    CurrencyRate it = (CurrencyRate) iterator.next();
                    newCurrencyRates.add(new CurrencyRate(it.getCurrency(), it.getRate(), it.getRate() * this.baseValue));
                }
            }

            this.currencyRates.setValue(newCurrencyRates);

        }
    }


}