package com.umarqureshi.api.network.interfaces;

import com.umarqureshi.api.network.response.FleetAroundResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface FleetApiInterface {

    @GET("/")
    Observable<FleetAroundResponse> getFleetAround(@Query("p1Lat") double boundOneLat, @Query("p1Lon") double boundOneLon, @Query("p2Lat") double boundTwoLat, @Query("p2Lon") double boundTwoLon);
}
