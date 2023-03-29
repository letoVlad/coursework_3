package com.example.coursework_3.services;

import com.example.coursework_3.model.ColorSocks;
import com.example.coursework_3.model.SizeSocks;
import com.example.coursework_3.model.Socks;

import java.util.List;

public interface SocksService {
    Socks addSocks(Socks socks);

    void takeSocks(Socks socks);

    List<Socks> getSocks(int cottonMin, int cottonMax);

    boolean deleteSocks(ColorSocks color, SizeSocks size, int cottonPart, int quantity);

    List<Socks> getAllSocks();
}
