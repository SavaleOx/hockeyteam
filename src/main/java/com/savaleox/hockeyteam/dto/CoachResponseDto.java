package com.savaleox.hockeyteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Response DTO for coach data")
public class CoachResponseDto {

    @Schema(description = "Unique identifier of the coach", example = "1")
    private Long id;

    @Schema(description = "First name of the coach", example = "Mike")
    private String name;

    @Schema(description = "Last name of the coach", example = "Babcock")
    private String surname;

    @Schema(description = "Age of the coach in years", example = "45")
    private Integer age;

    @Schema(description = "Coach's preferred tactical approach", example = "Offensive pressure system")
    private String tactic;

    @Schema(description = "Name of the team the coach manages", example = "Victoria Maple Leafs")
    private String teamName;
}