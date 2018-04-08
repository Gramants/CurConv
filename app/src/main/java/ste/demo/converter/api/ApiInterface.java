package ste.demo.converter.api;


import ste.demo.converter.api.model.LatestRates;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface ApiInterface {

    @GET("latest")
    Call<LatestRates> getLatestRates(@Query("base") String baserate);

}