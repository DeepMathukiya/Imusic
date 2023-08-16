package com.example.imusic;

public class AudioModel {
    private String Title;
    private String path;

    AudioModel(String title, String path) {
        this.Title = title;
        this.path = path;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}