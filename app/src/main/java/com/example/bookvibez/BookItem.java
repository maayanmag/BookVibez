package com.example.bookvibez;


import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class BookItem {
    private int id;
    private String title;
    private String author;
    private String genre;
    private String ownerName;
    private int ownerImg;
    private int bookImg;
    private String location;
    private LatLng latLng;
    private HashMap<User, Comment> timeline;

    public BookItem(int id, String title, String author, int src) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.bookImg = src;
        ownerName = "Tom Hanks";
        genre = "no genre defined";
        ownerImg = R.mipmap.man_icon;
    }



    public BookItem(int id, String title, String author, int src, LatLng lng, String snp, String bookGenre) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.bookImg = src;
        ownerName = "Tom Hanks";
        genre = bookGenre;
        ownerImg = R.mipmap.man_icon;
        latLng = lng;
        location = snp;
        timeline = new HashMap<User, Comment>();
    }

    public String getSLocation() {
        return location;
    }

    public void setSLocation(String location) {
        this.location = location;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public int getBookImg() {
        return bookImg;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public int getOwnerImg() {
        return ownerImg;
    }

    // Constructor to convert JSON object into a Java class instance
    public BookItem(JSONObject object){
        try {
            this.title = object.getString("title");
            this.author = object.getString("author");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Factory method to convert an array of JSON objects into a list of objects
    // User.fromJson(jsonArray);
    public static ArrayList<BookItem> fromJson(JSONArray jsonObjects) {
        ArrayList<BookItem> users = new ArrayList<>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                users.add(new BookItem(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return users;
    }


}
