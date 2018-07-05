package com.example.swatijain.mytest.ApiIntegration;

import com.example.swatijain.mytest.Responce.ListOfVendor;
import com.example.swatijain.mytest.Responce.ListProductResponse;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiClient {

    @GET("getProduct")
    Call<ListProductResponse> GetProductList();

    @GET("getVendor")
    Call<ListOfVendor> UserList();
}
