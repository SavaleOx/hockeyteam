package com.savaleox.hockeyteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Response DTO for player data")
public class PlayerResponseDto {

    @Schema(description = "Идентификатор игрока", example = "1")
    private Long id;

    @Schema(description = "Полное имя игрока (имя + фамилия)", example = "Виталий Пинчук")
    private String fullName;

    @Schema(description = "Игровой номер", example = "97")
    private Integer number;

    @Schema(description = "Возраст игрока", example = "25")
    private Integer age;

    @Schema(description = "Количество голов забитых за карьеру", example = "15")
    private Integer goals;

    @Schema(description = "Количество ассистов отданных за карьеру", example = "25")
    private Integer assists;

    @Schema(description = "Количество очков заработанных за карьеру (голы + ассисты)", example = "40")
    private Integer points;

    @Schema(description = "Название команды игрока", example = "Victoria Maple Leafs")
    private String teamName;

    @Schema(description = "Игровая позиция", example = "FORWARD")
    private String positionName;
}