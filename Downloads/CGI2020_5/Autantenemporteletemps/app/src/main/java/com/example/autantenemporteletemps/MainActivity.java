package com.example.autantenemporteletemps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    TextView view_city;
    TextView view_temp;
    TextView view_desc;

    ImageView view_weather;
    EditText search;
    FloatingActionButton search_float;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        view_city=findViewById(R.id.town);
        view_city.setText("");
        view_temp=findViewById(R.id.temp_max);
        view_temp.setText("");
        view_desc=findViewById(R.id.desc);
        view_desc.setText("");

        view_weather=findViewById(R.id.weather_image);
        search=findViewById(R.id.search_edit);
        search_float =findViewById(R.id.floating_search);

        search_float.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //hide Keyboard
                InputMethodManager imm=(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getRootView().getWindowToken(),0);
                api_key(String.valueOf(search.getText()));
            }
        });


    }



    private void api_key(final String City) {
        OkHttpClient client=new OkHttpClient();


        Request request=new Request.Builder()
                .url("https://api.openweathermap.org/data/2.5/weather?q="+City+"&appid=f0c60002d1c8c89f69fd0ffa45a8a6a1&units=metric")
                .get()
                .build();


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            Response response= client.newCall(request).execute();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String responseData= response.body().string();
                    try {
                        JSONObject json=new JSONObject(responseData);
                        JSONArray array=json.getJSONArray("weather");
                        JSONObject object=array.getJSONObject(0);

                        String description=object.getString("description");
                        String icons = object.getString("icon");

                        JSONObject temp1= json.getJSONObject("main");
                        Double Temperature=temp1.getDouble("temp");

                        setText(view_city,City);

                        String temps=Math.round(Temperature)+" °C";
                        setText(view_temp,temps);
                        setText(view_desc,description);
                        setImage(view_weather,icons);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }catch (IOException e){
            e.printStackTrace();
        }


    }
    private void setText(final TextView text, final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                text.setText(value);
            }
        });
    }
    private void setImage(final ImageView imageView, final String value){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //paste switch
                switch (value){
                    case "01d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.i01d));
                        break;
                    case "01n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.i01n));
                        break;
                    case "02d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.i02d));
                        break;
                    case "02n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.i02n));
                        break;
                    case "03d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.i03d));
                        break;
                    case "03n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.i03n));
                        break;
                    case "04d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.i04d));
                        break;
                    case "04n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.i04n));
                        break;
                    case "09d": imageView.setImageDrawable(getResources().getDrawable(R.drawable.i09d));
                        break;
                    case "09n": imageView.setImageDrawable(getResources().getDrawable(R.drawable.i09n));
                        break;
                    default:
                        imageView.setImageDrawable(getResources().getDrawable(R.drawable.shadow));

                }
            }
        });
    }
}