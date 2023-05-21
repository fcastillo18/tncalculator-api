package com.tncalculator.tncalculatorapi;

import com.tncalculator.tncalculatorapi.model.Operation;
import com.tncalculator.tncalculatorapi.model.OperationRequest;
import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.services.impl.OperationServiceImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TestUtil {

    public Operation getOperation(Operation.OperationType operationType, BigDecimal operationCost){
        return Operation.builder()
                .type(operationType)
                .cost((operationCost))
                .build();
    }

    // This important util will perform the operations and will make us to have a single test that will dynamically evaluate all operations
    public BigDecimal performOperation(Operation.OperationType operationType, BigDecimal num1, BigDecimal num2) {
        return switch (operationType) {
            case ADDITION -> num1.add(num2);
            case SUBTRACTION -> num1.subtract(num2);
            case MULTIPLICATION -> num1.multiply(num2);
            case DIVISION -> num1.divide(num2, RoundingMode.HALF_UP);
            case SQUARE_ROOT -> BigDecimal.valueOf(Math.sqrt(num1.doubleValue()));
            case RANDOM_STRING -> BigDecimal.valueOf(Math.random() * 1000).setScale(2, RoundingMode.HALF_UP);
        };
    }

    public Record getRecordByOperationType(OperationRequest request, Operation.OperationType operationType,
                                            OperationServiceImpl operationServiceImpl) {
        return switch (operationType) {
            case ADDITION -> operationServiceImpl.add(request);
            case SUBTRACTION -> operationServiceImpl.subtract(request);
            case MULTIPLICATION -> operationServiceImpl.multiply(request);
            case DIVISION -> operationServiceImpl.divide(request);
            case SQUARE_ROOT -> operationServiceImpl.squareRoot(request);
            case RANDOM_STRING -> operationServiceImpl.randomString(request);
        };
    }
}
