package com.example.bookvibez;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BookItem {
    private int id;
    private String title;
    private String author;

    public BookItem(int id, String title, String author) {
        this.id = id;
        this.title = title;
        this.author = author;
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
        ArrayList<BookItem> users = new ArrayList<BookItem>();
        for (int i = 0; i < jsonObjects.length(); i++) {
            try {
                users.add(new BookItem(jsonObjects.getJSONObject(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return users;
    }


    public static ArrayList<BookItem> getBooks() {
        ArrayList<BookItem> users = new ArrayList<BookItem>();
        users.add(new BookItem(1,"Harry", "San Diego"));
        users.add(new BookItem(2,"Marla", "San Francisco"));
        users.add(new BookItem(3,"Sarah", "San Marco"));
        return users;
    }

}