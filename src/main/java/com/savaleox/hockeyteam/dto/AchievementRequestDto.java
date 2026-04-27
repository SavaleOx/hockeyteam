package com.savaleox.hockeyteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request DTO for creating/updating an achievement")
public class AchievementRequestDto {

    @NotBlank(message = "Achievement name is required")
    @Size(min = 3, max = 255, message = "Achievement name must be between 3 and 255 characters")
    @Schema(description = "Название достижения", example = "Обладатель кубка Гагарина",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Size(max = 255, message = "Description cannot exceed 255 characters")
    @Schema(description = "Описание достижения", example = "Выиграть континентальную хоккейную лигу")
    private String description;
}