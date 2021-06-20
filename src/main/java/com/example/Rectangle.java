package com.example;

public class Rectangle {
    private String name;
    private Integer width;
    private Integer height;
    private String colour;

    public String getName() {
        return this.name;
    }

    public String getColour() {
        return this.colour;
    }

    public Integer getWidth() {
        return this.width;
    }

    public Integer getHeight() {
        return this.height;
    }

    public void setName(String n) {
        this.name = n;
    }

    public void setWidth(Integer w) {
        this.width = w;
    }

    public void setHeight(Integer h) {
        this.height = h;
    }

    public void setColour(String c) {
        this.colour = c;
    }
}
