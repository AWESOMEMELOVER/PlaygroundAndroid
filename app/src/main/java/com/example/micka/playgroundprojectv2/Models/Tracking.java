package com.example.micka.playgroundprojectv2.Models;

/**
 * Created by micka on 4/23/2018.
 */

public class Tracking {

    private String userBeaconName;
    private String playgroundName;
    private String imageUrl;
    private String playgroundId;
    private String notify;

    public String getUserBeaconName() {
        return userBeaconName;
    }

    public void setUserBeaconName(String userBeaconName) {
        this.userBeaconName = userBeaconName;
    }

    public String getPlaygroundName() {
        return playgroundName;
    }

    public void setPlaygroundName(String playgroundName) {
        this.playgroundName = playgroundName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPlaygroundId() {
        return playgroundId;
    }

    public void setPlaygroundId(String playgroundId) {
        this.playgroundId = playgroundId;
    }

    public String getNotify() {
        return notify;
    }

    public void setNotify(String notify) {
        this.notify = notify;
    }

}
