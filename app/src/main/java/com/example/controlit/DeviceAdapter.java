package com.example.controlit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.DeviceViewHolder> {

    private List<DeviceItem> deviceList;
    private Context context;
    private OnDeviceClickListener listener;

    public DeviceAdapter(Context context, List<DeviceItem> deviceList) {
        this.context = context;
        this.deviceList = deviceList;
        this.listener = listener;
    }
    public interface OnDeviceClickListener {
        void onMoreInfoClicked(DeviceItem device);
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_device, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
        DeviceItem device = deviceList.get(position);

        holder.deviceName.setText(device.getName());
        holder.deviceImage.setImageResource(device.getImageResId());

        //holder.moreInfoBtn.setOnClickListener(v -> listener.onMoreInfoClicked(device));
    }

    @Override
    public int getItemCount() {
        return deviceList.size();
    }

    static class DeviceViewHolder extends RecyclerView.ViewHolder {
        TextView deviceName;
        ImageView deviceImage;
        Button moreInfoBtn;

        public DeviceViewHolder(@NonNull View itemView) {
            super(itemView);
            deviceName = itemView.findViewById(R.id.deviceName);
            deviceImage = itemView.findViewById(R.id.deviceImage);
            moreInfoBtn = itemView.findViewById(R.id.btn_moreinfo);
        }
    }



}