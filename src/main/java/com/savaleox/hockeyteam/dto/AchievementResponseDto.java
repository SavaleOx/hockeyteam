package com.savaleox.hockeyteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Response DTO for achievement data")
public class AchievementResponseDto {

    @Schema(description = "Unique identifier of the achievement", example = "1")
    private Long id;

    @Schema(description = "Name of the achievement", example = "Stanley Cup Champion")
    private String name;

    @Schema(description = "Detailed description of the achievement", example = "Won the NHL Stanley Cup championship")
    private String description;
}