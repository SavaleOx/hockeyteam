package com.savaleox.hockeyteam.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Race condition demo result")
public class RaceConditionDemoDto {

    @Schema(description = "Количество потоков", example = "64")
    private int threads;

    @Schema(description = "Количество инкрементов на поток", example = "10000")
    private int incrementsPerThread;

    @Schema(description = "Ожидаемое значение счётчика", example = "640000")
    private int expected;

    @Schema(description = "Значение небезопасного счётчика", example = "523418")
    private int unsafeCounter;

    @Schema(description = "Значение synchronized счётчика", example = "640000")
    private int synchronizedCounter;

    @Schema(description = "Значение atomic счётчика", example = "640000")
    private int atomicCounter;

    @Schema(description = "Обнаружен ли race condition", example = "true")
    private boolean raceConditionDetected;
}