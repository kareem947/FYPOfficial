package com.example.fyp.Company.DriverRequests;

import android.content.Context;

public class RequestModel {
    private String name;
    private String mobile;
    Context context;


    public RequestModel(String name, String mobile) {
        this.context=context;
        this.name = name;
        this.mobile = mobile;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }
}
