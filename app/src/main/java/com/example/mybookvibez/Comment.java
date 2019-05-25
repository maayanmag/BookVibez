package com.example.mybookvibez;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.firestore.ServerTimestamp;
import java.sql.Timestamp;

@IgnoreExtraProperties
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
