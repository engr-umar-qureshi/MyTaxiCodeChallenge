package com.umarqureshi.api;

import com.umarqureshi.api.controller.FleetApi;
import com.umarqureshi.api.utils.ApiError;

import java.util.concurrent.TimeUnit;

import androidx.lifecycle.MutableLiveData;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyTaxiApiClient {

    private static final int CONNECTION_TIMEOUT_SECONDS = 15;
    private static final int READ_TIMEOUT_SECONDS = 30;
    private static final int WRITE_TIMEOUT_SECONDS = 30;

    private static Retrofit mRetrofitClient;
    private static MyTaxiApiClient myTaxiApiClient;

    public MyTaxiApiClient() {
        mRetrofitClient = new Retrofit.Builder()
                .baseUrl(BuildConfig.DEFAULT_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    public static MyTaxiApiClient getClient()
    {
        if(myTaxiApiClient ==null)
        {
            myTaxiApiClient = new MyTaxiApiClient();
        }

        return myTaxiApiClient;
    }


    public FleetApi getFleetApi(MutableLiveData<ApiError> liveDataError)
    {
        return new FleetApi(mRetrofitClient, liveDataError);
    }

    private OkHttpClient getOkHttpClient() {
        OkHttpClient client = new OkHttpClient()
                .newBuilder()
                .connectTimeout(CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
                .build();

        return client;
    }

}

