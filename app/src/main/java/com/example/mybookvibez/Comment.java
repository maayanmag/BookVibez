package com.example.mybookvibez;

import java.sql.Date;

public class Comment {
    private String comment;
    private int rate;
    private Date date;

    public Comment(String comment, int rate, Date date){
        this.comment =comment;
        this.rate = rate;
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
