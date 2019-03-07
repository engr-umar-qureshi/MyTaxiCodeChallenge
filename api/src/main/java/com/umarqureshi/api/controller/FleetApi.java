package com.umarqureshi.api.controller;

import com.umarqureshi.api.network.interfaces.FleetApiInterface;
import com.umarqureshi.api.network.model.FleetItem;
import com.umarqureshi.api.network.response.FleetAroundResponse;
import com.umarqureshi.api.network.utils.BaseApi;
import com.umarqureshi.api.utils.ApiError;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import retrofit2.Retrofit;

public class FleetApi extends BaseApi {

    FleetApiInterface mFleetInterface;
    MutableLiveData<ApiError> mLiveDataError;

    public FleetApi(Retrofit retrofitClient, MutableLiveData<ApiError> liveDataError) {
        mFleetInterface = retrofitClient.create(FleetApiInterface.class);
        mLiveDataError = liveDataError;
    }

    public void getFleetInBounds(double bound1Lat, double bound1Lon, double bound2Lat, double bound2Lon, MutableLiveData<List<FleetItem>> liveDataVehicle){

       Observable<FleetAroundResponse> observable = mFleetInterface.getFleetAround(bound1Lat, bound1Lon, bound2Lat, bound2Lon);

        Consumer<FleetAroundResponse> consumer = fleetAroundResponse -> {
            liveDataVehicle.setValue(fleetAroundResponse.getPoiList());
        };

        Consumer<Throwable> errorHandler = throwable -> {
            if(mLiveDataError != null) {
                mLiveDataError.setValue(ApiError.translateFailureToApiError(throwable));
            }
        };

        executeApiCall(observable, consumer, errorHandler);

    }

}
