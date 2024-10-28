package com.newsloop.newsloop_be.api.controller.model;

public class AuthResult {
    private String s3Url;
    private String hasUserData;
    private String name; // Add name field

    public AuthResult(String s3Url, String hasUserData, String name) { // Update constructor
        this.s3Url = s3Url;
        this.hasUserData = hasUserData;
        this.name = name;
    }

    public String getS3Url() {
        return s3Url;
    }

    public String hasUserData() {
        return hasUserData;
    }

    public String getName() { // Add getter for name
        return name;
    }

    // Setters (if needed)
    public void setS3Url(String s3Url) {
        this.s3Url = s3Url;
    }

    public void setHasUserData(String hasUserData) {
        this.hasUserData = hasUserData;
    }

    public void setName(String name) { // Add setter for name
        this.name = name;
    }
}