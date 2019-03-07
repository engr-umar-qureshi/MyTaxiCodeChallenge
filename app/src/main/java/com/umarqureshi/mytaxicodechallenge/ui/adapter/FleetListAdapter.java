package com.umarqureshi.mytaxicodechallenge.ui.adapter;

import android.app.Activity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umarqureshi.api.network.model.FleetItem;
import com.umarqureshi.mytaxicodechallenge.R;
import com.umarqureshi.mytaxicodechallenge.ui.activity.FleetMapActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class FleetListAdapter extends RecyclerView.Adapter<FleetListAdapter.VehicleViewHolder> {

    Activity mActivity;
    List<FleetItem> mListFleet;

    public FleetListAdapter(Activity context, List<FleetItem> listFleet) {
        this.mActivity = context;
        this.mListFleet = listFleet;
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.rv_item_fleet, parent, false);
        return new VehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        FleetItem fleetItem = mListFleet.get(position);

        holder.txtVehicleType.setText(fleetItem.getFleetType());
        int icon = FleetItem.FleetType.POOLING.equals(fleetItem.getFleetType())? R.drawable.pin_pooling : R.drawable.pin_taxi;
        holder.txtVehicleType.setCompoundDrawablesWithIntrinsicBounds(0,icon, 0, 0);

        holder.txtVehicleId.setText(Html.fromHtml("<b>Id:</b> "+ fleetItem.getId()));
        holder.txtVehicleLat.setText(Html.fromHtml("<b>Lat:</b> "+ fleetItem.getCoordinate().getLatitude()));
        holder.txtVehicleLon.setText(Html.fromHtml("<b>Lon:</b> "+ fleetItem.getCoordinate().getLongitude()));
        holder.txtVehicleHeading.setText(Html.fromHtml("<b>Heading:</b> "+ fleetItem.getHeading()));

        holder.viewHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FleetMapActivity.startMapActivity(mActivity, mListFleet, fleetItem.getId());
            }
        });

    }

    @Override
    public int getItemCount() {
        return mListFleet.size();
    }

    class VehicleViewHolder extends RecyclerView.ViewHolder {

        TextView txtVehicleType;
        TextView txtVehicleId;
        TextView txtVehicleLat;
        TextView txtVehicleLon;
        TextView txtVehicleHeading;
        View viewHolder;

        public VehicleViewHolder(View itemView) {
            super(itemView);

            txtVehicleType = itemView.findViewById(R.id.txt_vehicle_type);
            txtVehicleId = itemView.findViewById(R.id.txt_vehicle_id);
            txtVehicleLat = itemView.findViewById(R.id.txt_vehicle_lat);
            txtVehicleLon = itemView.findViewById(R.id.txt_vehicle_lon);
            txtVehicleHeading = itemView.findViewById(R.id.txt_vehicle_heading);

            viewHolder = itemView;
        }
    }
}