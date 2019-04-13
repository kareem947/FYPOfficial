package com.example.fyp.Customer;

public class CustomerCompletedOrderModel {

    private String driverName;
    private String imageView;
    private String requestId;
    private String orderName;
    private String time;

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setImageView(String imageView) {
        this.imageView = imageView;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getImageView() {
        return imageView;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getOrderName() {
        return orderName;
    }

    public String getTime() {
        return time;
    }

    public CustomerCompletedOrderModel(String driverName, String imageView, String requestId, String orderName, String time) {
        this.driverName = driverName;
        this.imageView = imageView;
        this.requestId = requestId;
        this.orderName = orderName;
        this.time = time;
    }
}
