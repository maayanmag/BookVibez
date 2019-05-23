package com.example.mybookvibez;


import com.google.android.gms.maps.model.LatLng;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;


public class BookItem {
    private String id, title, author, genre, ownerId, bookImg, location;
    private int points, giveaway;
    private LatLng latLng;
    private ArrayList<Comment> comments;

    public enum GIVE_BOOK {EXCHANGE, LEAVE}

    public BookItem(){

    }

    public BookItem(String title, String author, int src) {
        this.id = "0";
        this.title = title;
        this.author = author;
        genre = "no genre defined";
        comments = new ArrayList<>();
    }



    public BookItem(String title, String author, String bookGenre, int giveaway) {
        this.id = "";
        this.title = title;
        this.author = author;
        this.giveaway = giveaway;
        this.location = "";
        genre = bookGenre;
        latLng = null;
        comments = new ArrayList<>();

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

    public String getId() {
        return id;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getBookImg() {
        return bookImg;
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
