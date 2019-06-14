package com.example.mybookvibez;

import java.util.ArrayList;

public class User {
    private String name, id, vibeString, phoneNumber;
    private ArrayList<String> myBooks;
    private ArrayList<String> booksIRead;
    private int vibePoints;

    public User() {}

    public User(String name, String vibeString, String phoneNumber) {
        this.name = name;
        this.vibePoints = 0;
        this.vibeString = vibeString;
        this.phoneNumber = phoneNumber;
        myBooks = new ArrayList<>();
        booksIRead = new ArrayList<>();
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
