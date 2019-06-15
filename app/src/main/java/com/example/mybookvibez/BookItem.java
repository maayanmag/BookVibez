package com.example.mybookvibez;


import com.example.mybookvibez.BookPage.Comment;
import com.google.firebase.firestore.GeoPoint;
import java.util.ArrayList;

public class BookItem {

    private String id, title, author, genre, ownerId, location;
    private int points, giveaway, ownedBy;
    private boolean offered;
    private GeoPoint latLng;
    private ArrayList<Comment> comments;

    public BookItem() {}

    public BookItem(String title, String author, String bookGenre, String location, int giveaway) {
        this.id = "";
        this.title = title;
        this.author = author;
        this.giveaway = giveaway;
        this.location = location;
        this.genre = bookGenre;
        this.points = 0;
        this.latLng = null;
        this.comments = new ArrayList<>();
        this.ownedBy = 0;
        this.offered = true;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getGiveaway() {
        return giveaway;
    }

    public void setGiveaway(int giveaway) {
        this.giveaway = giveaway;
    }

    public GeoPoint getLatLng() {
        return latLng;
    }

    public void setLatLng(GeoPoint latLng) {
        this.latLng = latLng;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Comment> comments) {
        this.comments = comments;
    }

    public int getOwnedBy() {
        return this.ownedBy;
    }

    public void setOwnedBy(int ownedBy) {
        this.ownedBy = ownedBy;
    }

    public boolean getOffered() {
        return offered;
    }

    public void setOffered(boolean offered) {
        this.offered = offered;
    }  
}
