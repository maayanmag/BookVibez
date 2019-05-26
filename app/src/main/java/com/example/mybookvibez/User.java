package com.example.mybookvibez;

import java.util.ArrayList;

public class User {
    private String name, id, vibeString;
    private ArrayList<String> myBooks;
    private ArrayList<String> booksIRead;
    private int vibePoints;

    public User() {}

    public User(String name, int vibePoints, String vibeString) {
        this.name = name;
        this.vibePoints = vibePoints;
        this.vibeString = vibeString;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVibeString() {
        return vibeString;
    }

    public void setVibeString(String vibeString) {
        this.vibeString = vibeString;
    }

    public ArrayList<String> getMyBooks() {
        return myBooks;
    }

    public void setMyBooks(ArrayList<String> myBooks) {
        this.myBooks = myBooks;
    }

    public ArrayList<String> getBooksIRead() {
        return booksIRead;
    }

    public void setBooksIRead(ArrayList<String> booksIRead) {
        this.booksIRead = booksIRead;
    }

    public int getVibePoints() {
        return vibePoints;
    }

    public void setVibePoints(int vibePoints) {
        this.vibePoints = vibePoints;
    }
}
