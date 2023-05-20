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

    public OperationRequest(Long userId, double num1) {
        this.userId = userId;
        this.num1 = num1;
    }
}
