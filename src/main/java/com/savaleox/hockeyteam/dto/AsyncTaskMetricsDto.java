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
@Schema(description = "Async task counters")
public class AsyncTaskMetricsDto {

    @Schema(description = "Количество отправленных задач", example = "10")
    private long submitted;

    @Schema(description = "Количество выполняющихся задач", example = "2")
    private long running;

    @Schema(description = "Количество успешно выполненных задач", example = "7")
    private long succeeded;

    @Schema(description = "Количество упавших задач", example = "1")
    private long failed;
}