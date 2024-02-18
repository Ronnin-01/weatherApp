package com.application.weatherforall.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static  final String BASE_URL ="https://api.openweathermap.org/data/2.5/";

    public static Retrofit getRetrofitClient(){
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build();
    }
}
