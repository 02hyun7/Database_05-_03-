package com.example.model;

public class Theater {
    private int theaterId;
    private int cityRegionId;
    private String name;
    private String address;

    public int getTheaterId() {
        return theaterId;
    }

    public void setTheaterId(int theaterId) {
        this.theaterId = theaterId;
    }

    public int getCityRegionId() {
        return cityRegionId;
    }

    public void setCityRegionId(int cityRegionId) {
        this.cityRegionId = cityRegionId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
