package com.tncalculator.tncalculatorapi.payload.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OperationResponse {

    private Long id;
    private String operationType;
    private BigDecimal operationResult;
    private BigDecimal operationCost;
    private BigDecimal userRemainingBalance;

}
