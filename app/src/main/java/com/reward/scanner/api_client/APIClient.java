package com.reward.scanner.api_client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.reward.scanner.BuildConfig;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Raghvendra Sahu on 26-Apr-20.
 */
public class APIClient {


    public static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

        public static Api_Call getClient(String baseUrl) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .client(getRetrofitClient())
                    .build();

            return retrofit.create(Api_Call.class);
        }

        private static OkHttpClient getRetrofitClient() {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder client = new OkHttpClient.Builder();
            client.readTimeout(60, TimeUnit.SECONDS);
            client.connectTimeout(60, TimeUnit.SECONDS);
            if (BuildConfig.DEBUG)
                client.addInterceptor(logging);
            return client.build();
        }
}
