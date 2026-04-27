package com.savaleox.hockeyteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request DTO for creating/updating a team")
public class TeamRequestDto {

    @NotBlank(message = "Team name is required")
    @Size(min = 2, max = 50, message = "Team name must be between 2 and 50 characters")
    @Schema(description = "Название команды", example = "Динамо",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "City is required")
    @Size(min = 2, max = 170, message = "City name must be between 2 and 170 characters")
    @Schema(description = "Город в котором базируется команда", example = "Минск",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String city;
}