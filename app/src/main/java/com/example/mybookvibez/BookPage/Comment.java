package com.example.mybookvibez.BookPage;



public class Comment {
    private String time;
    private String comment;
    private String publisherId;

    public Comment() {}

    public Comment(String comment, String publisherId){
        this.comment = comment;
        this.publisherId = publisherId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
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
