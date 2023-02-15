package com.kumoh19.e_map;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Toilet {

    public String toiletName;
    public String toiletAddress;
    public String toiletTime;
    public double toiletLatitude;
    public double toiletLongitude;

    public Toilet() {}

    public Toilet(String toiletName, String toiletAddress, String toiletTime, double toiletLatitude, double toiletLongitude) {
        this.toiletName = toiletName;
        this.toiletAddress = toiletAddress;
        this.toiletTime = toiletTime;
        this.toiletLatitude = toiletLatitude;
        this.toiletLongitude = toiletLongitude;
    }

    public String getToiletName() { return toiletName; }
    public void setToiletName(String toiletName) {
        this.toiletName = toiletName;
    }

    public String getToiletAddress() { return toiletAddress; }
    public void setToiletAddress(String toiletAddress) {
        this.toiletAddress = toiletAddress;
    }

    public String getToiletTime() { return toiletTime; }
    public void setToiletTime(String toiletTime) {
        this.toiletTime = toiletTime;
    }

    public double getToiletLatitude() { return toiletLatitude; }
    public void setToiletLatitude(double toiletLatitude) {
        this.toiletLatitude = toiletLatitude;
    }

    public double getToiletLongitude() { return toiletLongitude; }
    public void setToiletLongitude(double toiletLongitude) {
        this.toiletLongitude = toiletLongitude;
    }

    @Override
    public String toString() {
        return "Trashcan{" +
                "Name='" + toiletName + '\'' +
                "Address='" + toiletAddress + '\'' +
                "Time='" + toiletTime + '\'' +
                '}';
    }

}
