package com.savaleox.hockeyteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Response DTO for achievement data")
public class AchievementResponseDto {

    @Schema(description = "Уникальный id достижения", example = "1")
    private Long id;

    @Schema(description = "Название достижения", example = "Обладатель кубка Гагарина")
    private String name;

    @Schema(description = "Описание достижения", example = "Выиграть континентальную хоккейную лигу")
    private String description;
}