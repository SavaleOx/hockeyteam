package com.savaleox.hockeyteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Response DTO for coach data")
public class CoachResponseDto {

    @Schema(description = "Уникальный идентификатор тренера", example = "1")
    private Long id;

    @Schema(description = "Имя тренера", example = "Дмитрий")
    private String name;

    @Schema(description = "Фамилия тренера", example = "Квартальнов")
    private String surname;

    @Schema(description = "Возраст тренера", example = "45")
    private Integer age;

    @Schema(description = "Предпочитаемая тактика", example = "гегенпрессинг")
    private String tactic;

    @Schema(description = "Название команды тренера", example = "Динамо")
    private String teamName;
}