package com.example.coursework_3.controllers;

import com.example.coursework_3.model.ColorSocks;
import com.example.coursework_3.model.SizeSocks;
import com.example.coursework_3.model.Socks;
import com.example.coursework_3.services.SocksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/socks")
@Tag(name = "Работа с носками", description = "CRUD- операции для работы с носками")
public class SocksController {
    private final SocksService socksService;

    public SocksController(SocksService socksService) {
        this.socksService = socksService;
    }

    @Operation(summary = "Регистрирует приход товара на склад.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200 ", description = "Удалось добавить приход;"),
            @ApiResponse(responseCode = "400", description = "Параметры запроса отсутствуют или имеют некорректный формат"),
            @ApiResponse(responseCode = "500 ", description = "Произошла ошибка, не зависящая от вызывающей стороны.")})
    @PostMapping("/{socks}")
    public ResponseEntity<Socks> addNewSocks(@RequestBody Socks socks) {
        socksService.addSocks(socks);
        return socks != null ? ResponseEntity.ok(socks) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Регистрирует отпуск носков со склада.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200 ", description = "удалось произвести отпуск носков со склада;"),
            @ApiResponse(responseCode = "400", description = "товара нет на складе в нужном количестве или параметры запроса имеют некорректный формат;"),
            @ApiResponse(responseCode = "500 ", description = "произошла ошибка, не зависящая от вызывающей стороны.")})
    @PutMapping
    public ResponseEntity<Socks> takeSocks(@RequestBody Socks socks) {
        socksService.takeSocks(socks);
        return socks != null ? ResponseEntity.ok(socks) : ResponseEntity.notFound().build();
    }

    @Operation(summary = "Возвращает общее количество носков на складе, соответствующих переданным в параметрах критериям запроса.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200 ", description = "запрос выполнен, результат в теле ответа в виде строкового представления целого числа;"),
            @ApiResponse(responseCode = "400", description = "параметры запроса отсутствуют или имеют некорректный формат;"),
            @ApiResponse(responseCode = "500 ", description = "произошла ошибка, не зависящая от вызывающей стороны.")})
    @GetMapping("/{cottonMin}/{cottonMax}")
    public ResponseEntity<List<Socks>> getSocks(@PathVariable int cottonMin, @PathVariable int cottonMax) {
        List<Socks> socks = socksService.getSocks(cottonMin, cottonMax);
        return ResponseEntity.ok(socks);
    }

    @Operation(summary = "Выводит список всех носков")
    @GetMapping
    public ResponseEntity<List<Socks>> getAllSocks() {
        List<Socks> allIngredients = socksService.getAllSocks();
        return ResponseEntity.ok(allIngredients);
    }

    @Operation(summary = "Регистрирует списание испорченных (бракованных) носков.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200 ", description = "запрос выполнен, товар списан со склада;"),
            @ApiResponse(responseCode = "400", description = "параметры запроса отсутствуют или имеют некорректный формат;"),
            @ApiResponse(responseCode = "500 ", description = "произошла ошибка, не зависящая от вызывающей стороны. ")})
    @DeleteMapping("/{color}/{size}/{cottonPart}/{quantity}")
    public ResponseEntity<Boolean> deleteSocks(@PathVariable ColorSocks color, @PathVariable SizeSocks size, @PathVariable int cottonPart, @PathVariable int quantity) {
        return socksService.deleteSocks(color, size, cottonPart, quantity) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}
