package com.epsilon.FunwithStatus.utills;

public class SubAlbum {
    private String name;
    private int thumbnail;

    public SubAlbum() {
    }

    public SubAlbum(String name,  int thumbnail) {
        this.name = name;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
