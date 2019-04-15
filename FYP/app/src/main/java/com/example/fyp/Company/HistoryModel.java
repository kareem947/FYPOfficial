package com.example.fyp.Company;

public class HistoryModel {

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

    public void setTime(String time) {
        this.time = time;
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

    public String getTime() {
        return time;
    }

    public HistoryModel(String driverName, String requestId, String customerName, String time) {
        this.driverName = driverName;
        this.requestId = requestId;
        this.customerName = customerName;
        this.time = time;
    }

    private String requestId;
    private String customerName;
    private String time;

}
