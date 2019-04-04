package com.example.micka.playgroundprojectv2.Models;

/**
 * Created by micka on 1/16/2018.
 */

public class Beacon {

    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String imgUrl;
    private String name;

    @Override
    public String toString() {
        return "Beacon{" +
                "id='" + id + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
