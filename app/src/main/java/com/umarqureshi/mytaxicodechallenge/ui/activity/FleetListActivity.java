package com.umarqureshi.mytaxicodechallenge.ui.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.model.LatLng;
import com.umarqureshi.api.network.model.FleetItem;
import com.umarqureshi.mytaxicodechallenge.R;
import com.umarqureshi.mytaxicodechallenge.ui.adapter.FleetListAdapter;
import com.umarqureshi.mytaxicodechallenge.ui.util.BaseActivity;
import com.umarqureshi.mytaxicodechallenge.ui.viewmodel.FleetViewModel;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;

public class FleetListActivity extends BaseActivity<FleetViewModel> {

    public static final LatLng INITIAL_BOUND1 = new LatLng(53.694865, 9.757589);
    public static final LatLng INITIAL_BOUND2 = new LatLng(53.394655, 10.099891);

    @BindView(R.id.rv_fleet)
    RecyclerView mRvFleet;

    FleetListAdapter mFleetAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fleet_list);
        initializeViewModel(FleetViewModel.class);

        initFleetList();

        populateFleetList();

    }

    private void initFleetList() {
        mRvFleet.setHasFixedSize(true);
        mRvFleet.setLayoutManager(new LinearLayoutManager(this));
    }

    private void populateFleetList()
    {
        showDialogNetworkProgress();
        mViewModel.getVehiclesInRegion(INITIAL_BOUND1.latitude, INITIAL_BOUND1.longitude, INITIAL_BOUND2.latitude, INITIAL_BOUND2.longitude).observe(this, listFleet -> {
            mFleetAdapter = new FleetListAdapter(FleetListActivity.this, listFleet);
            mRvFleet.setAdapter(mFleetAdapter);
            hideDialogNetworkProgress();
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.fleet_list_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_fleet_map) {
            mViewModel.getVehiclesInRegion(INITIAL_BOUND1.latitude, INITIAL_BOUND1.longitude, INITIAL_BOUND2.latitude, INITIAL_BOUND2.longitude).observe(this, this::startFleetMapActivity);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void startFleetMapActivity(List<FleetItem> fleetItems) {
        FleetMapActivity.startMapActivity(FleetListActivity.this, fleetItems);
    }
}