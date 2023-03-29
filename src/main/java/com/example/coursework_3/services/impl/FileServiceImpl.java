package com.example.coursework_3.services.impl;

import com.example.coursework_3.services.FileService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class FileServiceImpl implements FileService {
    @Value("${path.to.data.file}")
    private String dataFilePath;
    @Value("${socks.of.data.file}")
    private String dataFileSocks;

    @Override
    public boolean saveToFileSocks(String json) {
        try {
            cleanDataFileSocks();
            Files.writeString(Path.of(dataFilePath, dataFileSocks), json);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean cleanDataFileSocks() {
        try {
            Path path = Path.of(dataFilePath, dataFileSocks);
            Files.deleteIfExists(path);
            Files.createFile(path);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public String readFromFileSocks() {
        try {
            return Files.readString(Path.of(dataFilePath, dataFileSocks));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
