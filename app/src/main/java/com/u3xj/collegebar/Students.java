package com.u3xj.collegebar;

import android.graphics.Bitmap;

public class Students
{
    private String name;
    private String gender;
    private Bitmap thumbnail;
    private String status,collegename;

    public Students() {
    }

    public Students(String name, String gender, Bitmap thumbnail,String status, String collegename) {
        this.name = name;
        this.gender = gender;
        this.thumbnail = thumbnail;
        this.status = status;
        this.collegename = collegename;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCollegename() {
        return collegename;
    }

    public void setCollegename(String collegename) {
        this.status = collegename;
    }

}
