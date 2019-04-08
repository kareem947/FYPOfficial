package com.example.fyp.Customer;

import de.hdodenhof.circleimageview.CircleImageView;

public class CustomerAcceptedOrderModel {

    private String driverName;
    private String imageView;
    private String driverMobile;

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setImageView(String  imageView) {
        this.imageView = imageView;
    }

    public void setDriverMobile(String driverMobile) {
        this.driverMobile = driverMobile;
    }

    public String getDriverName() {
        return driverName;
    }

    public String  getImageView() {
        return imageView;
    }

    public String getDriverMobile() {
        return driverMobile;
    }

    public CustomerAcceptedOrderModel(String driverName, String  imageView, String driverMobile) {
        this.driverName = driverName;
        this.imageView = imageView;
        this.driverMobile = driverMobile;
    }
}
