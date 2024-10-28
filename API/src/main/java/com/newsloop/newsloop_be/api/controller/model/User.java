package com.newsloop.newsloop_be.api.controller.model;
import java.util.ArrayList;

public class User {

    private int id;
    private String name;
    private String email;
    private String s3_url;

    private ArrayList<String> interests_1 = new ArrayList<String>();
    private ArrayList<String> interests_2 = new ArrayList<String>();
    private ArrayList<String> interests_3 = new ArrayList<String>();

    // Constructor
    public User(int id, String name, String email, ArrayList<String> interests_1, ArrayList<String> interests_2, ArrayList<String> interests_3, String s3_url) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.interests_1 = interests_1;
        this.interests_2 = interests_2;
        this.interests_3 = interests_3;
        this.s3_url = s3_url;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getS3_url() {
        return s3_url;
    }

    public ArrayList<String> getInterests_1() {
        return interests_1;
    }

    public ArrayList<String> getInterests_2() {
        return interests_2;
    }

    public ArrayList<String> getInterests_3() {
        return interests_3;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setS3_url(String s3_url) {
        this.s3_url = s3_url;
    }

    public void setInterests_1(ArrayList<String> interests_1) {
        this.interests_1 = interests_1;
    }

    public void setInterests_2(ArrayList<String> interests_2) {
        this.interests_2 = interests_2;
    }

    public void setInterests_3(ArrayList<String> interests_3) {
        this.interests_3 = interests_3;
    }
}