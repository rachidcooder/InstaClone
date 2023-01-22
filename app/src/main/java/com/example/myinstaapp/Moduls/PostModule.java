package com.example.myinstaapp.Moduls;

public class PostModule {
    private String description;
    private String imagUrl;
    private String postid;
    private String publisher;

    public PostModule() {
    }

    public PostModule(String description, String imagUrl, String postid, String publisher) {
        this.description = description;
        this.imagUrl = imagUrl;
        this.postid = postid;
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagUrl() {
        return imagUrl;
    }

    public void setImagUrl(String imagUrl) {
        this.imagUrl = imagUrl;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
