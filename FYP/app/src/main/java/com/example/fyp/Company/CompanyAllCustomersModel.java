package com.example.fyp.Company;


import android.content.Context;

public class CompanyAllCustomersModel {
    private String name;
    private String mobile;
    private String cId;

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {

        return image;
    }

    private String image;
    Context context;


    public void setcId(String cId) {
        this.cId = cId;
    }

    public String getcId() {

        return cId;
    }

    public CompanyAllCustomersModel(String name, String mobile, String image, String cId) {
        this.context=context;
        this.name = name;
        this.cId=cId;
        this.image=image;
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
