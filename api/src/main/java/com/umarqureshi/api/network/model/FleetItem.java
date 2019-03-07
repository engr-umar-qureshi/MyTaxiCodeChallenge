package com.umarqureshi.api.network.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import androidx.annotation.Nullable;
import androidx.annotation.StringDef;

public class FleetItem implements Parcelable
{
    @Retention(RetentionPolicy.SOURCE)
    @StringDef({
            FleetType.TAXI,
            FleetType.POOLING})
    public @interface FleetType {
        String TAXI = "TAXI";
        String POOLING = "POOLING";
    }

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("coordinate")
    @Expose
    private Position coordinate;

    @FleetType
    @SerializedName("fleetType")
    @Expose
    private String fleetType;

    @SerializedName("heading")
    @Expose
    private float heading;

    public FleetItem() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Position getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Position coordinate) {
        this.coordinate = coordinate;
    }

    public String getFleetType() {
        return fleetType;
    }

    public void setFleetType(String fleetType) {
        this.fleetType = fleetType;
    }

    public float getHeading() {
        return heading;
    }

    public void setHeading(float heading) {
        this.heading = heading;
    }

    public final static Parcelable.Creator<FleetItem> CREATOR = new Creator<FleetItem>() {


        @SuppressWarnings({
                "unchecked"
        })
        public FleetItem createFromParcel(Parcel in) {
            return new FleetItem(in);
        }

        public FleetItem[] newArray(int size) {
            return (new FleetItem[size]);
        }

    };

    protected FleetItem(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.coordinate = ((Position) in.readValue((Position.class.getClassLoader())));
        this.fleetType = ((String) in.readValue((String.class.getClassLoader())));
        this.heading = ((float) in.readValue((float.class.getClassLoader())));
    }


    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(coordinate);
        dest.writeValue(fleetType);
        dest.writeValue(heading);
    }

    public int describeContents() {
        return 0;
    }


    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof FleetItem)) {
            return false;
        }
        FleetItem vehicle = (FleetItem) obj;
        return id == vehicle.id;
    }
}