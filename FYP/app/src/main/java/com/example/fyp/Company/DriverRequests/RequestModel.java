package com.example.fyp.Company.DriverRequests;

import android.content.Context;

public class RequestModel {
    private String name;
    private String mobile;
    private String id;
    Context context;


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public RequestModel(String name, String mobile, String id) {
        this.context=context;
        this.name = name;
        this.id=id;
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
