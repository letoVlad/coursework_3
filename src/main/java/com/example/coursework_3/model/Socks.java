package com.example.coursework_3.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class Socks {
    private ColorSocks colorSocks;
    private SizeSocks sizeSocks;
    private int cottonPart;
    private int quantity;
}

