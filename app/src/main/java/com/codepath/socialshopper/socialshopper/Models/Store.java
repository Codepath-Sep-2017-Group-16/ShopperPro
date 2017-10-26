package com.codepath.socialshopper.socialshopper.Models;

/**
 * Created by rdeshpan on 10/25/2017.
 */

public class Store {
    public String name;
    public int imageResource;

    public Store(String name, int imageResource) {
        this.name = name;
        this.imageResource = imageResource;
    }

    public Store() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getimageResource() {
        return imageResource;
    }

    public void setimageResource(int imageResource) {
        this.imageResource = imageResource;
    }
}
