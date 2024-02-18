package com.application.weatherforall;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;
import com.airbnb.lottie.LottieAnimationView;
import com.application.weatherforall.Api.ApiInterface;
import com.application.weatherforall.Api.RetrofitClient;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    LottieAnimationView lottie;
    String temperatureS,humidityS,windS,sunriseS,sunsetS,sealevelS,conditionS,maxtempS,mintempS;
    SearchView searchView;
    TextView cityname,temp,condMain,maxTemp,tempmin,humidity,windspeed,conditions,sunrise,sunset,sea;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lottie=findViewById(R.id.lottieanm);
        searchView=findViewById(R.id.searchView);
        cityname=findViewById(R.id.cityName);
        temp=findViewById(R.id.tempr);
        condMain=findViewById(R.id.condMain);
        tempmin=findViewById(R.id.tempmin);
        maxTemp=findViewById(R.id.maxTemp);
        humidity=findViewById(R.id.humidity);
        windspeed=findViewById(R.id.windspeed);
        conditions=findViewById(R.id.conditions);
        sunrise=findViewById(R.id.sunrise);
        sunset=findViewById(R.id.sunset);
        sea=findViewById(R.id.sea);

        SearchCity();
        fetchWeatherData("mumbai");

        lottie.setAnimation(R.raw.sunny);
        lottie.playAnimation();
    }



    private void SearchCity() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (query!=null){
                    fetchWeatherData(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }
    private void fetchWeatherData( String cityName) {
        Retrofit retrofit = RetrofitClient.getRetrofitClient();
        ApiInterface apiInterface1 = retrofit.create(ApiInterface.class);
        Call<WeatherData> response = apiInterface1.getWeatherData(cityName,"db6201f73aa7edb72bf39ba2ee90cc5e","metric");
        response.enqueue(new Callback<WeatherData>() {
            @Override
            public void onResponse(Call<WeatherData> call, Response<WeatherData> response) {
                WeatherData responsebdy = response.body();
                if(response.isSuccessful() && responsebdy!= null){
                    temperatureS=String.valueOf(responsebdy.getMain().getTemp());
                    humidityS= String.valueOf(responsebdy.getMain().getHumidity());
                    windS= String.valueOf(responsebdy.getWind().getSpeed());
                    sunriseS= String.valueOf(responsebdy.getSys().getSunrise());
                    sunsetS= String.valueOf(responsebdy.getSys().getSunset());
                    sealevelS= String.valueOf(responsebdy.getMain().getPressure());
                    conditionS=responsebdy.getWeather().get(0).getMain();
                    maxtempS = String.valueOf(responsebdy.getMain().getTempMax());
                    mintempS= String.valueOf(responsebdy.getMain().getTempMin());


                    maxTemp.setText("Max Temp: " + maxtempS + "°C");
                    tempmin.setText("Min Temp: " + mintempS + "°C");
                    condMain.setText(conditionS);
                    humidity.setText(humidityS+" %");
                    windspeed.setText(windS+" m/s");
                    sunrise.setText(time(Long.valueOf(sunriseS)));
                    sunset.setText(time(Long.valueOf(sunsetS)));
                    sea.setText(sealevelS+" nPA");
                    temp.setText(temperatureS+" °C");
                    conditions.setText(conditionS);
                    cityname.setText(cityName);

                    changeAnimation(conditionS);


                }
            }

            @Override
            public void onFailure(Call<WeatherData> call, Throwable t) {

            }
        });
    }

    private void changeAnimation(String conditionS){
        Log.d("WeatherData","changeAnimation: conditionS = " + conditionS);
        switch (conditionS){
            case "Haze":
            case "Clouds":
            case "Partly Clouds":
            case"Overcast":
            case"Mist":
            case"Foggy":
            case"Smoke":
                getWindow().getDecorView().setBackgroundResource(R.drawable.cloudy);
                ((LottieAnimationView) findViewById(R.id.lottieanm)).setAnimation(R.raw.cloudy);
                break;

            case "Clear Sky":
            case"Sunny":
            case "Clear":
                getWindow().getDecorView().setBackgroundResource(R.drawable.sunny);
                ((LottieAnimationView) findViewById(R.id.lottieanm)).setAnimation(R.raw.sunny);
                break;

            case"Rain":
            case"Light Rain":
            case"Drizzle":
            case"Moderate Rain":
            case"Showers":
            case"Heavy Rain":
                getWindow().getDecorView().setBackgroundResource(R.drawable.rainyy);
                ((LottieAnimationView) findViewById(R.id.lottieanm)).setAnimation(R.raw.rain);
                break;

            case"Light Snow":
            case"Moderate Snow":
            case"Heavy Snow":
            case"Blizzard":

            default:
                getWindow().getDecorView().setBackgroundResource(R.drawable.sunny);
                ((LottieAnimationView) findViewById(R.id.lottieanm)).setAnimation(R.raw.sunny);
                break;
        }
        ((LottieAnimationView) findViewById(R.id.lottieanm)).playAnimation();

    }

    public String time(Long timestamp){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm",Locale.getDefault());
        return simpleDateFormat.format(new Date(timestamp*1000));

    }
}