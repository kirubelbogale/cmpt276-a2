package com.example;

public class Rectangle {
    private String name;
    private String width;
    private String height;
    private String colour;
    private String id;

    public String getName() {
        return this.name;
    }

    public String getColour() {
        return this.colour;
    }

    public String getWidth() {
        return this.width;
    }

    public String getHeight() {
        return this.height;
    }

    public String getID() {
        return this.id;
    }

    public void setName(String n) {
        this.name = n;
    }

    public void setWidth(String w) {
        this.width = w;
    }

    public void setHeight(String h) {
        this.height = h;
    }

    public void setColour(String c) {
        this.colour = c;
    }

    public void setID(String id) {
        this.id = id;
    }
}
