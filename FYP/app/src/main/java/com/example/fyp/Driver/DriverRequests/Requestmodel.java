package com.example.fyp.Driver.DriverRequests;

import de.hdodenhof.circleimageview.CircleImageView;

public class Requestmodel {

    private String name;
    private String pick;
    private String drop;
    private String imageView;
    private String id;

    public String getRequest() {
        return request;
    }

    private String request;
    private String payment;

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }


    public Requestmodel(String name, String pick, String drop,String id,String request,String imageView, String payment) {
        this.name = name;
        this.id=id;
        this.request=request;
        this.pick = pick;
        this.payment = payment;
        this.imageView=imageView;
        this.drop = drop;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }


    public void setPick(String pick) {
        this.pick = pick;
    }

    public void setDrop(String drop) {
        this.drop = drop;
    }

    public void setImageView(String imageView) {
        this.imageView = imageView;
    }

    public String getPick() {
        return pick;
    }

    public String getDrop() {
        return drop;
    }

    public String getImageView() {
        return imageView;
    }
}
