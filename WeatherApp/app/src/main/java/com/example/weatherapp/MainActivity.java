package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    //https://www.youtube.com/watch?reload=9&v=MOx2MJOpXdM
    EditText et;
    TextView tv;
    Button btn;

    String url = "api.openweathermap.org/data/2.5/weather?q={city name}&appid={API key}";
    String apiKey = "b855f746eba33d635366eb25685d0f99";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et = findViewById(R.id.et);
        tv = findViewById(R.id.tv);
        btn = findViewById(R.id.btn);
    }

    public void getWeather(View v){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        weatherapi myapi = retrofit.create(weatherapi.class);
        Call<Example> exampleCall = myapi.getWeather(et.getText().toString().trim(), apiKey);

        exampleCall.enqueue(new Callback<Example>() {
            @Override
            public void onResponse(Call<Example> call, Response<Example> response) {
                if (response.code() == 404){
                    Toast.makeText(MainActivity.this, "Please Enter a Valid City Name", Toast.LENGTH_SHORT).show();
                }
                else if (!(response.isSuccessful())){
                    Toast.makeText(MainActivity.this, response.code(), Toast.LENGTH_SHORT).show();
                }
                Example mydata = response.body();
                Main main = mydata.getMain();
                Double temp = main.getTemp();
                Integer temperature = (int)(temp-273.15);
                tv.setText(String.valueOf(temperature) + "C");
            }

            @Override
            public void onFailure(Call<Example> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}