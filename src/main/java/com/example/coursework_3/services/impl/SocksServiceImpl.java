package com.example.coursework_3.services.impl;

import com.example.coursework_3.model.ColorSocks;
import com.example.coursework_3.model.SizeSocks;
import com.example.coursework_3.model.Socks;
import com.example.coursework_3.services.FileService;
import com.example.coursework_3.services.SocksService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class SocksServiceImpl implements SocksService {
    private Map<ColorSocks, Map<Long, Socks>> socksList = new HashMap<>();
    private final FileService fileService;
    private final ResourceLoader resourceLoader;

    @Autowired
    public SocksServiceImpl(FileService fileService, @Autowired ResourceLoader resourceLoader) {
        this.fileService = fileService;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Socks addSocks(Socks socks) {
        ColorSocks colorSocks = socks.getColorSocks();
        Map<Long, Socks> socksByColor = socksList.getOrDefault(colorSocks, new HashMap<>());
        Long key = 0L;
        if (!socksByColor.isEmpty()) {
            key = Collections.max(socksByColor.keySet()) + 1;
        }
        socksByColor.put(key, socks);
        socksList.put(colorSocks, socksByColor);
        saveToFile();
        return socks;
    }

    @Override
    public void takeSocks(Socks socks) {
        ColorSocks color = socks.getColorSocks();
        Map<Long, Socks> socksByNumber = socksList.get(color);
        if (socksByNumber == null || !socksByNumber.containsValue(socks)) {
            throw new IllegalArgumentException("Таких носков нет");
        }
        for (Map.Entry<Long, Socks> entry : socksByNumber.entrySet()) {
            if (entry.getValue().equals(socks)) {
                if (entry.getValue().getQuantity() == socks.getQuantity()) {
                    socksByNumber.remove(entry.getKey());
                } else {
                    entry.getValue().setQuantity(entry.getValue().getQuantity() - socks.getQuantity());
                }
                saveToFile();
                break;
            }
        }
    }

    @Override
    public List<Socks> getSocks(int cottonMin, int cottonMax) {
        List<Socks> socksInRange = new ArrayList<>();
        for (Map.Entry<ColorSocks, Map<Long, Socks>> colorEntry : socksList.entrySet()) {
            for (Socks socks : colorEntry.getValue().values()) {
                if (socks.getCottonPart() >= cottonMin && socks.getCottonPart() <= cottonMax) {
                    socksInRange.add(socks);
                }
            }
        }
        return socksInRange.isEmpty() ? null : socksInRange;
    }

    @Override
    public boolean deleteSocks(ColorSocks color, SizeSocks size, int cottonPart, int quantity) {
        Map<Long, Socks> socksByNumber = socksList.get(color);
        if (socksByNumber == null) {
            return false;
        }
        Long foundNumber = null;
        for (Map.Entry<Long, Socks> entry : socksByNumber.entrySet()) {
            Socks socks = entry.getValue();
            if (socks.getSizeSocks() == size &&
                    socks.getCottonPart() == cottonPart &&
                    socks.getQuantity() == quantity) {
                foundNumber = entry.getKey();
                break;
            }
        }
        if (foundNumber == null) {
            return false;
        }
        socksByNumber.remove(foundNumber);
        saveToFile();
        return true;
    }

    private void saveToFile() {
        try {
            String json = new ObjectMapper().writeValueAsString(socksList);
            fileService.saveToFileSocks(json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private void readFromFile() {
        String json = fileService.readFromFileSocks();
        try {
            if (json == null || json.isEmpty()) {
                socksList = new HashMap<>();
            } else {
                socksList = new ObjectMapper().readValue(json, new TypeReference<Map<ColorSocks, Map<Long, Socks>>>() {
                });
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @PostConstruct
    private void init() {
        Resource resource = resourceLoader.getResource("classpath:socks.json");
        if (resource.exists()) {
            readFromFile();
        }
    }
}