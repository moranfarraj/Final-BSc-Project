package com.example.gatheringofgamers;

import java.sql.Date;
import java.util.ArrayList;

public class User {
    private final int userID;
    private String username;
    private String password;
    private Date birthDate;
    private ArrayList<String> languages;

    public User(int userID, String username, String password, Date birthDate, ArrayList<String> languages) {
        this.userID = userID;
        this.username = username;
        this.password = password;
        this.birthDate = birthDate;
        this.languages = languages;
    }

    public int getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public ArrayList<String> getLanguages() {
        return languages;
    }

    public void setLanguages(ArrayList<String> languages) {
        this.languages = languages;
    }

}
