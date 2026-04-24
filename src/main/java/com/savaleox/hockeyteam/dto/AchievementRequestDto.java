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
    @Size(min = 3, max = 100, message = "Achievement name must be between 3 and 100 characters")
    @Schema(description = "Name of the achievement", example = "Stanley Cup Champion",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    @Schema(description = "Detailed description of the achievement", example = "Won the NHL Stanley Cup championship")
    private String description;
}