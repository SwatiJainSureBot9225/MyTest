package com.example.swatijain.mytest.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.swatijain.mytest.ApiIntegration.ApiClient;
import com.example.swatijain.mytest.R;
import com.example.swatijain.mytest.Responce.ListProductResponse;
import com.example.swatijain.mytest.Responce.Product;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ViewProduct extends AppCompatActivity {

    int gProductName;
    private Dialog progress;
    Toolbar toolbar;
    Spinner gProductSpinner;
    Product[] gProductList;
    ArrayAdapter<String> gProductNameAdapter;
    ArrayList<String> gProductNameArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_view);

        //Tool Bar handle
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Products");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progress = new Dialog(this, android.R.style.Theme_Translucent);
        progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //here we set layout of progress dialog
        progress.setContentView(R.layout.progress_dialog);
        progress.setCancelable(true);

        gProductSpinner = (Spinner) findViewById(R.id.spinner_product);
        GetProductName();

    }

    public void GetProductName() {
        try {
            System.out.println("entering try block get Product");
            progress.show();
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client.addInterceptor(loggingInterceptor);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://surebotdemo.co/IMS_API/api/IMS_CI/")
                    .client(client.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            ApiClient request = retrofit.create(ApiClient.class);

            Call<ListProductResponse> call = request.GetProductList();
            call.enqueue(new Callback<ListProductResponse>() {
                @Override
                public void onResponse(Call<ListProductResponse> call, Response<ListProductResponse> response) {

                    if (response.isSuccessful()) {

                        ListProductResponse list = response.body();
                        gProductList = list.getRecords();

                        for (int i = 0; i < gProductList.length; i++) {
                            gProductNameArrayList = new ArrayList<>();

                            gProductNameArrayList.add(0, gProductList[0].getProduct_name_hint());

                            for (int j = 0; j <= i; j++) {
                                gProductNameArrayList.add(1, gProductList[j].getProduct_name());

                                gProductNameAdapter = new ArrayAdapter<>(ViewProduct.this, android.R.layout.simple_spinner_item, gProductNameArrayList);
                                gProductSpinner.setAdapter(gProductNameAdapter);
                                System.out.println("array value is" + gProductNameArrayList);

                                gProductSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                        gProductName = gProductSpinner.getSelectedItemPosition();
                                        gProductSpinner.setSelection(gProductName, false);
                                        // Toast.makeText(getApplicationContext(),item1 , Toast.LENGTH_LONG).show();
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> parent) {

                                    }
                                });
                            }

                        }

                    }
                    progress.dismiss();
                }

                @Override
                public void onFailure(Call<ListProductResponse> call, Throwable t) {
                    progress.cancel();
                    Toast.makeText(ViewProduct.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();

                }
            });
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("entering catch block now" + e);
        }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ViewProduct.this, MainActivity.class));
        super.onBackPressed();
        finish();
    }
}
