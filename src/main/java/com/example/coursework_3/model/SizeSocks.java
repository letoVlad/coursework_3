package com.example.coursework_3.model;

public enum SizeSocks {
    SIZE_35(35),
    SIZE_36(36),
    SIZE_36_5(36.5),
    SIZE_37(37),
    SIZE_37_5(37.5);

    private double size;

    SizeSocks(double size) {
        this.size = size;
    }

    public double getSize() {
        return size;
    }
}


