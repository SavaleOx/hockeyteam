package com.savaleox.hockeyteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Schema(description = "Response DTO for team data")
public class TeamResponseDto {

    @Schema(description = "Идентификатор команды", example = "1")
    private Long id;

    @Schema(description = "Название команды", example = "Динамо")
    private String name;

    @Schema(description = "Город в котором базируется команда", example = "Минск")
    private String city;

    @Schema(description = "Список идентификаторов игроков находящихся в этой команде", example = "[1, 2, 3, 4, 5]")
    private List<Long> playerIds;

    @Schema(description = "Идентификатор тренера команды", example = "1")
    private Long coachId;

    @Schema(description = "Полное имя тренера (имя + фамилия)", example = "Дмитрий Квартальнов")
    private String coachFullName;
}