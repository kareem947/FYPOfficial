package com.example.fyp.Driver;

public class DriverAllProgressingOrdersModel {


    private String driverName;
    private String customerId;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    private String imageView;
    private String driverMobile;
    private String requestId;

    public void setPickup(String pickup) {
        this.pickup = pickup;
    }

    public void setDropoff(String dropoff) {
        this.dropoff = dropoff;
    }

    public String getDropoff() {
        return dropoff;
    }

    public String getPickup() {
        return pickup;
    }

    private String pickup;
    private String dropoff;





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

    public void setRequestId(String driverId) {
        this.requestId = driverId;
    }

    public String getRequestId() {
        return requestId;
    }

    public DriverAllProgressingOrdersModel(String driverName, String  imageView, String driverMobile, String requestId,String CustomerId,String pickup,String dropoff) {
        this.driverName = driverName;
        this.imageView = imageView;
        this.driverMobile = driverMobile;
        this.requestId=requestId;
        this.pickup=pickup;
        this.dropoff=dropoff;
        this.customerId=CustomerId;
    }

}
