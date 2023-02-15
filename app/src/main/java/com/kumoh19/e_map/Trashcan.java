package com.kumoh19.e_map;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Trashcan {

    public String trashcanAddress;
    public String trashcanDetail;
    public double trashcanLatitude;
    public double trashcanLongitude;

    public Trashcan() {}

    public Trashcan(String trashcanAddress, String trashcanDetail, double trashcanLatitude, double trashcanLongitude) {
        this.trashcanAddress = trashcanAddress;
        this.trashcanDetail = trashcanDetail;
        this.trashcanLatitude = trashcanLatitude;
        this.trashcanLongitude = trashcanLongitude;
    }

    public String getTrashcanAddress() { return trashcanAddress; }
    public void setTrashcanAddress(String trashcanAddress) {
        this.trashcanAddress = trashcanAddress;
    }

    public String getTrashcanDetail() { return trashcanDetail; }
    public void setTrashcanDetail(String trashcanDetail) {
        this.trashcanDetail = trashcanDetail;
    }

    public double getTrashcanLatitude() { return trashcanLatitude; }
    public void setTrashcanLatitude(double trashcanLatitude) {
        this.trashcanLatitude = trashcanLatitude;
    }

    public double getTrashcanLongitude() { return trashcanLongitude; }
    public void setTrashcanLongitude(double trashcanLongitude) {
        this.trashcanLongitude = trashcanLongitude;
    }

    @Override
    public String toString() {
        return "Trashcan{" +
                "Address='" + trashcanAddress + '\'' +
                "Detail='" + trashcanDetail + '\'' +
                '}';
    }

}
