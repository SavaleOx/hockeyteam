package com.savaleox.hockeyteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request DTO for creating/updating a coach")
public class CoachRequestDto {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Schema(description = "Имя тренера", example = "Дмитрий", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "Surname is required")
    @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
    @Schema(description = "Фамилия тренера", example = "Квартальнов", requiredMode = Schema.RequiredMode.REQUIRED)
    private String surname;

    @NotNull(message = "Age is required")
    @Min(value = 18, message = "Coach must be at least 18 years old")
    @Max(value = 80, message = "Coach cannot be older than 80 years")
    @Schema(description = "Возраст тренера", example = "45", minimum = "18", maximum = "80")
    private Integer age;

    @Size(max = 255, message = "Tactic description cannot exceed 255 characters")
    @Schema(description = "Предпочитаемая тактика тренера", example = "Гегенпрессинг")
    private String tactic;

    @NotNull(message = "Team ID is required")
    @Positive(message = "Team ID must be positive")
    @Schema(description = "Идентификатор команды тренера", example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Long teamId;
}