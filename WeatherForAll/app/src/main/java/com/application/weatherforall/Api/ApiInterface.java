package com.application.weatherforall.Api;

import com.application.weatherforall.WeatherData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
    @GET("weather")
    Call<WeatherData> getWeatherData(
            @Query("q") String city,
            @Query("appid") String appid,
            @Query("units") String units
    );
}
