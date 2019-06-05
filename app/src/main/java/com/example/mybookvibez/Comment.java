package com.example.mybookvibez;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;


public class Comment {
    @ServerTimestamp
    private Date time;
    private String comment;
    private String publisherId;

    public Comment() {}

    public Comment(String comment, String publisherId){
        this.comment =comment;
        this.publisherId = publisherId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
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
}
