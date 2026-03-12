package com.savaleox.hockeyteam.controller;

import com.savaleox.hockeyteam.dto.PositionRequestDto;
import com.savaleox.hockeyteam.dto.PositionResponseDto;
import com.savaleox.hockeyteam.service.PositionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/positions")
public class PositionController {
    private final PositionService positionService;

    public PositionController(PositionService positionService) {
        this.positionService = positionService;
    }

    @GetMapping
    public List<PositionResponseDto> getAll() {
        return positionService.getAll();
    }

    @GetMapping("/{id}")
    public PositionResponseDto getById(@PathVariable Long id) {
        return positionService.getById(id);
    }

    @GetMapping("/by-name")
    public PositionResponseDto getByName(@RequestParam String name) {
        return positionService.getByName(name);
    }

    @PostMapping
    public PositionResponseDto create(@RequestBody PositionRequestDto dto) {
        return positionService.create(dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        positionService.delete(id);
    }
}