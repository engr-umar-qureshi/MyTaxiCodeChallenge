package com.umarqureshi.api.network.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Position implements Parcelable
{

    @SerializedName("latitude")
    @Expose
    private double latitude;

    @SerializedName("longitude")
    @Expose
    private double longitude;

    public Position() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public final static Parcelable.Creator<Position> CREATOR = new Creator<Position>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Position createFromParcel(Parcel in) {
            return new Position(in);
        }

        public Position[] newArray(int size) {
            return (new Position[size]);
        }

    };

    protected Position(Parcel in) {
        this.latitude = ((double) in.readValue((double.class.getClassLoader())));
        this.longitude = ((double) in.readValue((double.class.getClassLoader())));
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(latitude);
        dest.writeValue(longitude);
    }

    public int describeContents() {
        return 0;
    }

}