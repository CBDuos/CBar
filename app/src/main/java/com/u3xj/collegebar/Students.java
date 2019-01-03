package com.u3xj.collegebar;

import android.graphics.Bitmap;

public class Students
{
    private String name;
    private String gender;
    private Bitmap thumbnail;

    public Students() {
    }

    public Students(String name, String gender, Bitmap thumbnail) {
        this.name = name;
        this.gender = gender;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(int numOfSongs) {
        this.gender = gender;
    }

    public Bitmap getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.thumbnail = thumbnail;
    }
}
