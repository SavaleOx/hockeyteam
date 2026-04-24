package com.savaleox.hockeyteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Schema(description = "Response DTO for team data")
public class TeamResponseDto {

    @Schema(description = "Unique identifier of the team", example = "1")
    private Long id;

    @Schema(description = "Name of the hockey team", example = "Victoria Maple Leafs")
    private String name;

    @Schema(description = "City where the team is based", example = "Toronto")
    private String city;

    @Schema(description = "List of player IDs belonging to the team", example = "[1, 2, 3, 4, 5]")
    private List<Long> playerIds;

    @Schema(description = "ID of the team's coach", example = "1")
    private Long coachId;

    @Schema(description = "Full name of the team's coach", example = "Mike Babcock")
    private String coachFullName;
}