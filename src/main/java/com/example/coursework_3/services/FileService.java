package com.example.coursework_3.services;

import com.example.coursework_3.model.Socks;

public interface FileService {

    boolean saveToFileSocks(String json);

    boolean cleanDataFileSocks();

    String readFromFileSocks();
}
