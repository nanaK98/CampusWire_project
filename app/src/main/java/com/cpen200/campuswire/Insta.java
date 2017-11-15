package com.cpen200.campuswire;


public class Insta {
    private String username, title, desc, image;
    private long timestamp;

    public Insta() {
    }


    public Insta(String pic, String username, String title, String desc, String image) {
        this.username = username;
        this.title = title;
        this.desc = desc;
        this.image = image;

    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}