package com.example.coursework_3.model;

public enum ColorSocks {
    BLACK("Черный"),
    WHITE("Белый"),
    RED("Красный"),
    YELLOW("Желтый");

    private String color;

    ColorSocks(String color) {
        this.color = color;
    }

    public void setColor(String color) {
        this.color = color;
    }


}
