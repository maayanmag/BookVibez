package com.example.bookvibez;


import android.graphics.drawable.Drawable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BookItem {
    private int id;
    private String title;
    private String author;
    private int imageSrc;

    public BookItem(int id, String title, String author, int src) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.imageSrc = src;
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

    public int getImageSrc() {
        return imageSrc;
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