package ste.demo.converter.api;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import ste.demo.converter.api.model.LatestRates;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiRatesClient implements Callback<LatestRates> {

    private String BASE_URL = "https://revolut.duckdns.org/";
    private ApiInterface service;
    private MutableLiveData<LatestRates> latestRates = new MutableLiveData();


    public void start() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(ApiInterface.class);

    }


    public LiveData<LatestRates> getLatestUpdateRates() {
        return latestRates;
    }


    public void refreshLatestUpdateRates(String baseCurrency) {
        Call<LatestRates> call = service.getLatestRates(baseCurrency);
        call.enqueue(this);
    }

    @Override
    public void onResponse(Call<LatestRates> call, Response<LatestRates> response) {
        if (response.isSuccessful()) {
            latestRates.setValue(response.body());
        } else {

        }
    }

    @Override
    public void onFailure(Call<LatestRates> call, Throwable t) {
        t.printStackTrace();
    }
}
