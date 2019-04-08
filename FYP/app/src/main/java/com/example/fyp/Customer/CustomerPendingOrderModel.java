package com.example.fyp.Customer;

public class CustomerPendingOrderModel {

    private String name , mobile;

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

    public CustomerPendingOrderModel(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;
    }
}
