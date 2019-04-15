package com.example.fyp.Company;

public class ProgressingOrdersModel {

    private String driverName;

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }


    public String getDriverName() {
        return driverName;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getCustomerName() {
        return customerName;
    }


    public ProgressingOrdersModel(String driverName, String requestId, String customerName, String orderName) {
        this.driverName = driverName;
        this.requestId = requestId;
        this.customerName = customerName;
        this.orderName = orderName;
    }

    private String requestId;
    private String customerName;

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderName() {
        return orderName;
    }

    private String orderName;

}
