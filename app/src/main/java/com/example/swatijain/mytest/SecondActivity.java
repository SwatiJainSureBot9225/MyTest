package com.example.swatijain.mytest;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.swatijain.mytest.ApiIntegration.ApiClient;
import com.example.swatijain.mytest.Responce.ListOfVendor;
import com.example.swatijain.mytest.Responce.Vendor;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SecondActivity extends AppCompatActivity {

    int item1;
    private Dialog progress;
    Spinner gVendorSpinner;
    Toolbar toolbar;
    Vendor[] gVendorList;
    ArrayAdapter<String> gVendorNameAdapter;
    ArrayList<String> gVendorNameArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        //Tool Bar handle
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View Vendor Details");
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
        progress.setContentView(R.layout.progress_dialog);
        progress.setCancelable(true);

        gVendorSpinner = (Spinner) findViewById(R.id.spinner_vendor);
        GetVendorName();
    }

    public void GetVendorName() {
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
        ApiClient apiClient = retrofit.create(ApiClient.class);

        Call<ListOfVendor> call = apiClient.UserList();
        call.enqueue(new Callback<ListOfVendor>() {
            @Override
            public void onResponse(Call<ListOfVendor> call, Response<ListOfVendor> response) {

                if (response.isSuccessful()) {
                    ListOfVendor vendor = response.body();
                    gVendorList = vendor.getRecords();

                    for (int i = 0; i < gVendorList.length; i++) {
                        gVendorNameArrayList = new ArrayList<>();
                        gVendorNameArrayList.add(0, gVendorList[0].getVendor_name_hint());

                        for (int j = 0; j <= i; j++) {
                            gVendorNameArrayList.add(1, gVendorList[j].getVendor_name());

                            gVendorNameAdapter = new ArrayAdapter<>(SecondActivity.this, android.R.layout.simple_spinner_item, gVendorNameArrayList);
                            gVendorSpinner.setAdapter(gVendorNameAdapter);
                            System.out.println("array value is" + gVendorNameAdapter);

                            gVendorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    item1 = gVendorSpinner.getSelectedItemPosition();
                                    gVendorSpinner.setSelection(item1, false);

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
            public void onFailure(Call<ListOfVendor> call, Throwable t) {
                progress.cancel();
                Toast.makeText(SecondActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();


            }
        });

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(SecondActivity.this, MainActivity.class));
        super.onBackPressed();
        finish();
    }
}
