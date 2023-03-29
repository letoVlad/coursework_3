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
import org.springframework.web.bind.annotation.PathVariable;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SocksServiceImpl implements SocksService {
    private static int number = 0;
    private static Map<Integer, Socks> socksList = new HashMap<>();
    private final FileService fileService;
    private final ResourceLoader resourceLoader;

    @Autowired
    public SocksServiceImpl(FileService fileService, @Autowired ResourceLoader resourceLoader) {
        this.fileService = fileService;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public Socks addSocks(Socks socks) {
        if (socks.getCottonPart() >= 0 && socks.getCottonPart() <= 100 && socks.getQuantity() > 0 && socks.getQuantity() % 1 == 0) {
            socksList.put(number++, socks);
            saveToFile();
            return socks;
        } else {
            throw new IllegalArgumentException("Неверное значение количества носков.");
        }
    }

    @Override
    public void takeSocks(Socks socks) {
        List<Integer> keysToRemove = new ArrayList<>();
        for (Map.Entry<Integer, Socks> socksEntry : socksList.entrySet()) {
            if (socksEntry.getValue().equals(socks)) {
                keysToRemove.add(socksEntry.getKey());
            }
        }
        if (keysToRemove.isEmpty()) {
            throw new IllegalArgumentException("Таких носков нет");
        }
        for (Integer key : keysToRemove) {
            socksList.remove(key);
            saveToFile();
        }
    }

    @Override
    public List<Socks> getSocks(int cottonMin, int cottonMax) {
        List<Socks> keysToGetNumber = new ArrayList<>();
        for (Map.Entry<Integer, Socks> socksEntry : socksList.entrySet()) {
            if (socksEntry.getValue().getCottonPart() >= cottonMin && socksEntry.getValue().getCottonPart() <= cottonMax) {
                keysToGetNumber.add(socksEntry.getValue());
            }
        }
        if (keysToGetNumber.isEmpty()) {
            return null;
        } else {
            return keysToGetNumber;
        }
    }

    @Override
    public boolean deleteSocks(ColorSocks color, SizeSocks size, int cottonPart, int quantity) {
        for (Map.Entry<Integer, Socks> socksEntry : socksList.entrySet()) {
            if (socksEntry.getValue().getColorSocks().equals(color) &&
                    socksEntry.getValue().getSizeSocks().equals(size) &&
                    socksEntry.getValue().getCottonPart() == cottonPart &&
                    socksEntry.getValue().getQuantity() == quantity) {
                socksList.remove(socksEntry.getKey());
                saveToFile();
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Socks> getAllSocks() {
        return new ArrayList<>(socksList.values());
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
            socksList = new ObjectMapper().readValue(json, new TypeReference<Map<Integer, Socks>>() {
            });
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
