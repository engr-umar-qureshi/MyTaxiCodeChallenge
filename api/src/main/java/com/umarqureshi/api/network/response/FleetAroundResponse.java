package com.umarqureshi.api.network.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.umarqureshi.api.network.model.FleetItem;

import java.util.List;

public class FleetAroundResponse {

    @SerializedName("poiList")
    @Expose
    private List<FleetItem> poiList = null;

    public List<FleetItem> getPoiList() {
        return poiList;
    }

    public void setPoiList(List<FleetItem> poiList) {
        this.poiList = poiList;
    }
}
