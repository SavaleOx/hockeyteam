package com.savaleox.hockeyteam.service;

import com.savaleox.hockeyteam.dto.PositionRequestDto;
import com.savaleox.hockeyteam.dto.PositionResponseDto;
import com.savaleox.hockeyteam.mapper.PositionMapper;
import com.savaleox.hockeyteam.model.entity.Position;
import com.savaleox.hockeyteam.repository.PositionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PositionService {
    private final PositionRepository positionRepository;
    private final PositionMapper positionMapper;

    public PositionService(PositionRepository positionRepository, PositionMapper positionMapper) {
        this.positionRepository = positionRepository;
        this.positionMapper = positionMapper;
    }

    public List<PositionResponseDto> getAll() {
        return positionRepository.findAll().stream()
                .map(positionMapper::toResponseDto)
                .toList();
    }

    public PositionResponseDto getById(Long id) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));
        return positionMapper.toResponseDto(position);
    }

    public PositionResponseDto getByName(String name) {
        Position position = positionRepository.findByName(name);

        return positionMapper.toResponseDto(position);
    }

    @Transactional
    public PositionResponseDto create(PositionRequestDto dto) {
        Position position = positionMapper.toEntity(dto);
        Position saved = positionRepository.save(position);
        return positionMapper.toResponseDto(saved);
    }

    @Transactional
    public void delete(Long id) {

        positionRepository.deleteById(id);
    }

    @Transactional
    public PositionResponseDto update(Long id, PositionRequestDto dto) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));
        position.setName(dto.getName());
        Position saved = positionRepository.save(position);
        return positionMapper.toResponseDto(saved);
    }

    @Transactional
    public PositionResponseDto patch(Long id, PositionRequestDto dto) {
        Position position = positionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Position not found"));
        if (dto.getName() != null) {
            position.setName(dto.getName());
        }
        Position saved = positionRepository.save(position);
        return positionMapper.toResponseDto(saved);
    }
}