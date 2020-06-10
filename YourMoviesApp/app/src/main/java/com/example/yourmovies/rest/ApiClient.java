package com.example.yourmovies.rest;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    public static final String BASE_URL = "http://31.192.104.124:8080/";

//    static OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
//        @Override
//        public Response intercept(Chain chain) throws IOException {
//            Request newRequest = chain.request().newBuilder()
//                    .addHeader("Authorization",
//                            "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhbGV4YW5kZXIiLCJleHAiOjE1ODYyNzM3ODEsImlhdCI6MTU4NjA5Mzc4MX0.PNauUq6LOyUcU1W_O9RU88CUtH_s-WE1ThBR-rSxdha8Cghop6ImIxaMXdmKs2MmeroAfc8XXzFDTOQiBIkysg")
//                    .build();
//            return chain.proceed(newRequest);
//        }
//    }).build();

    public static Retrofit getClient(final String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder()
                                .addHeader("Authorization",
                                        "Bearer " + token)
                                .build();
                        return chain.proceed(newRequest);
                    }
                }).build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

    public static Retrofit getClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }

}
