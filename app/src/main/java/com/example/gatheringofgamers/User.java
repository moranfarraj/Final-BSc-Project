package com.example.gatheringofgamers;
import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Objects;

public class User implements Serializable {
    public String name;
    public String password;
    public int age;
    public String img;

    public String gender;
    private String id;
    public String location;
    public double score;

    public User(String id,String name, String gender, String location) {
        this.id =id ;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.location = location;
    }
    public User(String id,String name, String gender, String location,String img) {
        this.id =id ;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.location = location;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public User(String name, String email, String password, int age, String gender, String location, String interests) {
        this.name = name;
        this.password = password;
        this.age = age;
        this.gender = gender;
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

}





