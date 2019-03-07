package com.umarqureshi.mytaxicodechallenge.ui.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.umarqureshi.api.network.model.FleetItem;
import com.umarqureshi.mytaxicodechallenge.BaseApplication;
import com.umarqureshi.mytaxicodechallenge.R;
import com.umarqureshi.mytaxicodechallenge.ui.util.BaseActivity;
import com.umarqureshi.mytaxicodechallenge.ui.viewmodel.FleetViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.umarqureshi.mytaxicodechallenge.ui.activity.FleetListActivity.INITIAL_BOUND1;
import static com.umarqureshi.mytaxicodechallenge.ui.activity.FleetListActivity.INITIAL_BOUND2;

public class FleetMapActivity extends BaseActivity<FleetViewModel> implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    public static final String EXTRA_FLEET_LIST = BaseApplication.PACKAGE_NAME+".ui.activity.FleetMapActivity.extra_fleet_list";
    public static final String EXTRA_FLEET_ITEM_ID = BaseApplication.PACKAGE_NAME+".ui.activity.FleetMapActivity.extra_fleet_item_id";

    private final float FOCUS_ZOOM_LEVEL = 15.0f;
    private final int FOCUS_TILT = 0;

    SupportMapFragment mMapFragment;

    private GoogleMap mMap;

    private Map<String, Marker> mFleetMarkers = new HashMap<>();

    private BitmapDescriptor mMarkerIconTaxi, mMarkerIconPooling;

    private String mSelectedFleetItem;
    private List<FleetItem> mListFleet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fleet_map);
        initializeViewModel(FleetViewModel.class);

        loadMarkerIcons();

        mMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fleet_map);
        mMapFragment.getMapAsync(this);


        mSelectedFleetItem = getIntent().getStringExtra(EXTRA_FLEET_ITEM_ID);
        mListFleet = getIntent().getParcelableArrayListExtra(EXTRA_FLEET_LIST);

        if(savedInstanceState != null) {
            mSelectedFleetItem = savedInstanceState.getString(EXTRA_FLEET_ITEM_ID);
        }

        if(mListFleet == null) {
            populateFleetList();
        } else  if(mSelectedFleetItem != null && mFleetMarkers.containsKey(mSelectedFleetItem)){
            animateMarkerToCenter(mFleetMarkers.get(mSelectedFleetItem));
        }
    }

    private void loadMarkerIcons()
    {
        mMarkerIconTaxi = BitmapDescriptorFactory.fromResource(R.drawable.map_pin_taxi);
        mMarkerIconPooling = BitmapDescriptorFactory.fromResource(R.drawable.map_pin_pooling);
    }

    private void populateFleetList() {
        showDialogNetworkProgress();
        mViewModel.getVehiclesInRegion(INITIAL_BOUND1.latitude, INITIAL_BOUND1.longitude, INITIAL_BOUND2.latitude, INITIAL_BOUND2.longitude).observe(this, this::displayFleetOnMap);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        if(mListFleet != null){
            displayFleetOnMap(mListFleet);
        }
    }

    private void displayFleetOnMap(List<FleetItem> listFleet) {
        hideDialogNetworkProgress();

        if(mMap == null){
            return;
        }

        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (FleetItem fleetItem : listFleet) {
            Marker marker;
                MarkerOptions markerOptions = new MarkerOptions()
                        .position(new LatLng(fleetItem.getCoordinate().getLatitude(), fleetItem.getCoordinate().getLongitude()))
                        .icon(fleetItem.getFleetType().equals(FleetItem.FleetType.TAXI)? mMarkerIconTaxi: mMarkerIconPooling)
                        .rotation(fleetItem.getHeading())
                        .anchor(0.5f, 0.5f);

                marker = mMap.addMarker(markerOptions);
                float infoAnchorX = (float) (Math.sin(-fleetItem.getHeading() * Math.PI / 180) * 0.5 + 0.5);
                float infoAnchorY = (float) -(Math.cos(-fleetItem.getHeading() * Math.PI / 180) * 0.5 - 0.5);
                marker.setInfoWindowAnchor(infoAnchorX, infoAnchorY);

                marker.setTitle(fleetItem.getFleetType());
                marker.setSnippet("Id: "+ fleetItem.getId());
            if (marker != null) {
                mFleetMarkers.put(fleetItem.getId(), marker);
                builder.include(marker.getPosition());
            }
        }

        if(mSelectedFleetItem != null && mFleetMarkers.containsKey(mSelectedFleetItem)){
            animateMarkerToCenter(mFleetMarkers.get(mSelectedFleetItem));
        } else {
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), getResources().getDimensionPixelSize(R.dimen.space_long_gap)));
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(EXTRA_FLEET_ITEM_ID, mSelectedFleetItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        animateMarkerToCenter(marker);
        mSelectedFleetItem = marker.getSnippet().split("Id: ")[1];
        return true;
    }

    private void animateMarkerToCenter(Marker selectedMarker)
    {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(selectedMarker.getPosition())
                .zoom(FOCUS_ZOOM_LEVEL)
                .bearing(selectedMarker.getRotation())
                .tilt(FOCUS_TILT)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        selectedMarker.showInfoWindow();
    }

    public static void startMapActivity(Activity activity, List<FleetItem> listFleet){
        Intent fleetMapIntent = new Intent(activity, FleetMapActivity.class);
        fleetMapIntent.putParcelableArrayListExtra(EXTRA_FLEET_LIST, new ArrayList<>(listFleet));
        activity.startActivity(fleetMapIntent);
    }

    public static void startMapActivity(Activity activity, List<FleetItem> listFleet, String fleetItemId){
        Intent fleetMapIntent = new Intent(activity, FleetMapActivity.class);
        fleetMapIntent.putParcelableArrayListExtra(FleetMapActivity.EXTRA_FLEET_LIST, new ArrayList<>(listFleet));
        fleetMapIntent.putExtra(FleetMapActivity.EXTRA_FLEET_ITEM_ID, fleetItemId);
        activity.startActivity(fleetMapIntent);
    }
}