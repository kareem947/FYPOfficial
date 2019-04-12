package com.example.fyp.Customer;

public class CustomerPendingOrderModel {

    private String name;
    private String uid;

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getUid() {
        return uid;
    }

    public String getRequestId() {
        return requestId;
    }

    private String requestId;
    private String mobile;

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderName() {
        return orderName;
    }

    private String orderName;

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public CustomerPendingOrderModel(String name, String mobile,String orderName,String uid,String requestId) {
        this.name = name;
        this.mobile = mobile;
        this.orderName=orderName;
        this.uid=uid;
        this.requestId=requestId;
    }
}
