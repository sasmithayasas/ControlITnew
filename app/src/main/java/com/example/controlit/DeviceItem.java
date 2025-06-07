package com.example.controlit;

public class DeviceItem {

    private String name;
    private int imageResId;

    public DeviceItem(String name, int imageResId) {
        this.name = name;
        this.imageResId = imageResId;
    }

    public String getName() {
        return name;
    }

    public int getImageResId() {
        return imageResId;
    }
}
