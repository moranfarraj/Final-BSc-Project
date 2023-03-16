package com.example.gatheringofgamers;
import java.sql.Date;
import java.util.ArrayList;
public class User {
    public String name;
    public String email;
    public String password;
    public int age;
    public String gender;
    public String location;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String name, String email, String password, int age, String gender, String location, String interests) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.location = location;
    }
}





