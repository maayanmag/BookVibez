package com.example.mybookvibez;

import java.sql.Timestamp;

public class Comment {
    private String comment;
    private Timestamp time;
    private String image;
    private String publisherName;
    private String publisherId;

    public Comment() {}

    public Comment(String comment, Timestamp time, String publisherId, String name, String pic){
        this.comment =comment;
        this.time = time;
        this.image = pic;
        this.publisherId = publisherId;
        this.publisherName = name;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPublisherName() {
        return publisherName;
    }

    public void setPublisherName(String publisherName) {
        this.publisherName = publisherName;
    }

    public String getPublisherId() {
        return publisherId;
    }

    public void setPublisherId(String publisherId) {
        this.publisherId = publisherId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Timestamp getDate() {
        return time;
    }

    public void setDate(Timestamp time) {
        this.time = time;
    }
}
