package com.example.bookvibez;

import java.util.ArrayList;

public class User {
    private String name;
    private ArrayList<BookItem> myBooks;
    private ArrayList<BookItem> booksIRead;
    private int vibePoints;
    private String vibeString;
    private int userImg;

    public User(String name, int p, int userImg){
        this.name = name;
        this.myBooks = new ArrayList<>();
        this.booksIRead  = new ArrayList<>();
        this.vibePoints = p;
        this.vibeString = "hi";
        this.userImg = userImg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<BookItem> getMyBooks() {
        return myBooks;
    }

    public void setMyBooks(ArrayList<BookItem> myBooks) {
        this.myBooks = myBooks;
    }

    public ArrayList<BookItem> getBooksIRead() {
        return booksIRead;
    }

    public void setBooksIRead(ArrayList<BookItem> booksIRead) {
        this.booksIRead = booksIRead;
    }

    public String getVibePointsString() {
        return "" + vibePoints;
    }

    public int getVibePoints() {
        return vibePoints;
    }

    public void setVibePoints(int vibePoints) {
        this.vibePoints = vibePoints;
    }

    public String getVibeString() {
        return vibeString;
    }

    public void setVibeString(String vibeString) {
        this.vibeString = vibeString;
    }

    public int getUserImg() {
        return userImg;
    }

    public void setUserImg(int userImg) {
        this.userImg = userImg;
    }
}
