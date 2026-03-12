package com.savaleOx.hockeyteam.service;

import com.savaleOx.hockeyteam.dto.PositionRequestDto;
import com.savaleOx.hockeyteam.dto.PositionResponseDto;
import com.savaleOx.hockeyteam.mapper.PositionMapper;
import com.savaleOx.hockeyteam.model.entity.Position;
import com.savaleOx.hockeyteam.repository.PositionRepository;
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
        if (position == null) {
            throw new RuntimeException("Position not found");
        }
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
        if (!positionRepository.existsById(id)) {
            throw new RuntimeException("Position not found");
        }
        positionRepository.deleteById(id);
    }
}