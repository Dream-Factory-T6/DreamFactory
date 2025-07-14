package com.DreamFactory.DF.swagger;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Error response")
public class ErrorResponse {
    @Schema(example = "404")
    private int status;

    @Schema(example = "User not found")
    private String message;
}