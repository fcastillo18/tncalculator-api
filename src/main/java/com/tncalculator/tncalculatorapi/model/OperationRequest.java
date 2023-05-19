package com.tncalculator.tncalculatorapi.model;

import lombok.*;

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
    private Long operationId;
}
