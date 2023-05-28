package com.tncalculator.tncalculatorapi.payload.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Optional;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class OperationRequest {
    private Long userId;
    private double num1;
    private double num2;
    @Schema(example = "string", accessMode = Schema.AccessMode.READ_ONLY) // hide the property in swagger
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<String> randomString;

    public OperationRequest(Long userId, double num1) {
        this.userId = userId;
        this.num1 = num1;
    }

    public OperationRequest(long l, double num1, double num2) {
        this.userId = l;
        this.num1 = num1;
        this.num2 = num2;
    }

    public OperationRequest(Long userId, String randomString) {
        this.userId = userId;
        this.randomString = Optional.ofNullable(randomString);
    }
}
