package com.umarqureshi.mytaxicodechallenge.ui.viewmodel;

import com.umarqureshi.api.MyTaxiApiClient;
import com.umarqureshi.api.controller.FleetApi;
import com.umarqureshi.api.network.model.FleetItem;
import com.umarqureshi.mytaxicodechallenge.ui.util.BaseViewModel;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class FleetViewModel extends BaseViewModel {

    private MutableLiveData<List<FleetItem>> mListVehicles;
    private String mLastLoadedFleetBounds;
    private FleetApi mFleetApi;

    public LiveData<List<FleetItem>> getVehiclesInRegion(double bound1Lat, double bound1Lon, double bound2Lat, double bound2Lon){
        String fleetBounds = bound1Lat+","+bound1Lon+","+bound2Lat+","+bound2Lon;
        if(mListVehicles == null) {
            mListVehicles = new MutableLiveData<>();
            mLastLoadedFleetBounds = fleetBounds;
            loadVehiclesInRegion(bound1Lat, bound1Lon, bound2Lat, bound2Lon);
        } else if(!mLastLoadedFleetBounds.equals(fleetBounds)) {
            loadVehiclesInRegion(bound1Lat, bound1Lon, bound2Lat, bound2Lon);
        }

        return mListVehicles;
    }

    private void loadVehiclesInRegion(double bound1Lat, double bound1Lon, double bound2Lat, double bound2Lon)
    {
        getFleetApi().getFleetInBounds(bound1Lat, bound1Lon, bound2Lat, bound2Lon, mListVehicles);
    }

    private FleetApi getFleetApi() {
        if(mFleetApi == null) {
            mFleetApi = MyTaxiApiClient.getClient().getFleetApi(mApiError);
        }

        return mFleetApi;
    }
}
