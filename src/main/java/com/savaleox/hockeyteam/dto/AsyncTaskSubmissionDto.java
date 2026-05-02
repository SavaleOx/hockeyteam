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
@Schema(description = "Async task submit response")
public class AsyncTaskSubmissionDto {

    @Schema(description = "Уникальный идентификатор задачи", example = "4c76e2ff-b52e-4573-9d97-86827a37f91a")
    private String taskId;
}