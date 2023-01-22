package com.example.myinstaapp.Moduls;

public class UserModle {
    private String id;
    private String userName;
    private String name;
    private String email;
    private String imageUrl;
    private String bio;



    public UserModle() {

    }

    public UserModle(String id, String userName, String name, String email, String imageUrl, String bio) {
        this.id = id;
        this.userName = userName;
        this.name = name;
        this.email = email;
        this.imageUrl = imageUrl;
        this.bio = bio;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}
