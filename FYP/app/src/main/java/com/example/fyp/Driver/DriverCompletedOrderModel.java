package com.example.fyp.Driver;

public class DriverCompletedOrderModel {

    private String customerName;
    private String imageView;
    private String requestId;
    private String customerMobile;
    private String time;

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setImageView(String imageView) {
        this.imageView = imageView;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setCustomerMobile(String customerMobile) {
        this.customerMobile = customerMobile;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCustomerName() {
        return customerName;
    }

    public String getImageView() {
        return imageView;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getCustomerMobile() {
        return customerMobile;
    }

    public String getTime() {
        return time;
    }

    public DriverCompletedOrderModel(String customerName, String imageView, String requestId, String customerMobile, String time) {
        this.customerName = customerName;
        this.imageView = imageView;
        this.requestId = requestId;
        this.customerMobile = customerMobile;
        this.time = time;
    }
}
