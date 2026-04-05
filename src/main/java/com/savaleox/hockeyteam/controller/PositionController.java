package com.savaleox.hockeyteam.controller;

import com.savaleox.hockeyteam.dto.PositionRequestDto;
import com.savaleox.hockeyteam.dto.PositionResponseDto;
import com.savaleox.hockeyteam.service.PositionService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @PutMapping("/{id}")
    public PositionResponseDto update(@PathVariable Long id, @RequestBody PositionRequestDto dto) {
        return positionService.update(id, dto);
    }

    @PatchMapping("/{id}")
    public PositionResponseDto patch(@PathVariable Long id, @RequestBody PositionRequestDto dto) {
        return positionService.patch(id, dto);
    }
}