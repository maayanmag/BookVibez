package com.example.mybookvibez;

import java.sql.Timestamp;

public class Comment {
    private String comment;
    private Timestamp time;

    public Comment() {}

    public Comment(String comment, Timestamp time){
        this.comment =comment;
        this.time = time;
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
