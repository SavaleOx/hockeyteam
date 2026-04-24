package com.savaleox.hockeyteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Request DTO for creating/updating a player")
public class PlayerRequestDto {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    @Schema(description = "First name of the player", example = "Connor", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "Surname is required")
    @Size(min = 2, max = 50, message = "Surname must be between 2 and 50 characters")
    @Schema(description = "Last name of the player", example = "McDavid", requiredMode = Schema.RequiredMode.REQUIRED)
    private String surname;

    @NotNull(message = "Number is required")
    @Min(value = 1, message = "Number must be at least 1")
    @Max(value = 99, message = "Number must be at most 99")
    @Schema(description = "Player's jersey number", example = "97", minimum = "1", maximum = "99")
    private Integer number;

    @NotNull(message = "Age is required")
    @Min(value = 16, message = "Player must be at least 16 years old")
    @Max(value = 50, message = "Player cannot be older than 50 years")
    @Schema(description = "Age of the player in years", example = "25", minimum = "16", maximum = "50")
    private Integer age;

    @NotNull(message = "Team ID is required")
    @Positive(message = "Team ID must be positive")
    @Schema(description = "ID of the team the player belongs to", example = "1",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Long teamId;

    @NotBlank(message = "Position is required")
    @Pattern(regexp = "GOALKEEPER|DEFENDER|FORWARD",
            message = "Position must be GOALKEEPER, DEFENDER, or FORWARD")
    @Schema(description = "Playing position", example = "FORWARD",
            allowableValues = {"GOALKEEPER", "DEFENDER", "FORWARD"})
    private String position;

    @NotNull(message = "Goals are required")
    @Min(value = 0, message = "Goals cannot be negative")
    @Max(value = 100, message = "Goals cannot exceed 100")
    @Schema(description = "Total goals scored in the current season", example = "15", minimum = "0", maximum = "100")
    private Integer goals;

    @NotNull(message = "Assists are required")
    @Min(value = 0, message = "Assists cannot be negative")
    @Max(value = 100, message = "Assists cannot exceed 100")
    @Schema(description = "Total assists made in the current season", example = "25", minimum = "0", maximum = "100")
    private Integer assists;
}