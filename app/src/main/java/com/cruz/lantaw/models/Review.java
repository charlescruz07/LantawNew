package com.cruz.lantaw.models;

/**
 * Created by Acer on 28/09/2017.
 */

public class Review {
    private String userImage;
    private String userName;
    private String userComment;
    private String time;

    public Review() {
    }

    public Review(String userImage, String userName, String userComment, String time) {
        this.userImage = userImage;
        this.userName = userName;
        this.userComment = userComment;
        this.time = time;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
