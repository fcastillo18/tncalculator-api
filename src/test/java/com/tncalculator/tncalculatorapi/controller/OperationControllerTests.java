package com.tncalculator.tncalculatorapi.controller;

import com.tncalculator.tncalculatorapi.TestUtil;
import com.tncalculator.tncalculatorapi.model.Operation;
import com.tncalculator.tncalculatorapi.model.OperationRequest;
import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.model.User;
import com.tncalculator.tncalculatorapi.services.impl.OperationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OperationControllerTests {
    @Mock
    private OperationServiceImpl operationService;

    @InjectMocks
    private OperationController operationController;

    private final TestUtil  testUtil = new TestUtil();

    @DisplayName("Endpoint Tests")
    @ParameterizedTest(name = "Testing ENDPOINTS, OperationType is {0}")
    @EnumSource(Operation.OperationType.class) // It will iterate all operation, and it will run this same test for all of them
    public void testEndpoint(Operation.OperationType operationType) {
        System.out.println("OPERATION: "+ operationType);
        long userId = 1L;
        double num1 = 10.0;
        double num2 = 5.0;
        BigDecimal amount = testUtil.performOperation(operationType, BigDecimal.valueOf(num1), BigDecimal.valueOf(num2));
        BigDecimal userBalance = BigDecimal.valueOf(100.0);
        BigDecimal operationCost = BigDecimal.valueOf(10.0);

        // printing some info:
        System.out.println("value1: "+ num1 + " value2: " + num2);
        System.out.println("OPERATION: "+ operationType);
        System.out.println("Total amount: "+ amount);
        System.out.println("-----------------------------------------");

        // Setting the necessary properties in the user
        User user = User.builder()
                .id(userId)
                .balance(userBalance)
                .build();

        // Setting the necessary properties in the request
        OperationRequest request = new OperationRequest(userId, num1, num2);

        // Setting the necessary properties in the operation
        Operation operation = testUtil.getOperation(operationType, operationCost);

        // Setting the expected properties in the record
        Record expectedRecord = Record.builder()
                .user(user)
                .operation(operation)
                .amount(amount)
                .build();

        when(operationService.subtract(any(OperationRequest.class))).thenReturn(expectedRecord);

        Record result = operationController.subtract(request);

        // assertions to verify that the result matches the expected record
        Assertions.assertEquals(expectedRecord.getId(), result.getId());
        Assertions.assertEquals(expectedRecord.getUser(), result.getUser());
        Assertions.assertEquals(expectedRecord.getOperation(), result.getOperation());
        Assertions.assertEquals(expectedRecord.getAmount(), result.getAmount());
    }


    @Test
    public void testGetAllOperations() {
        // Mock the data
        Operation operation1 = Operation.builder()
                .id(1L)
                .type(Operation.OperationType.ADDITION)
                .cost(BigDecimal.valueOf(10))
                .build();

        Operation operation2 = Operation.builder()
                .id(2L)
                .type(Operation.OperationType.SUBTRACTION)
                .cost(BigDecimal.valueOf(10))
                .build();

        List<Operation> mockOperations = Arrays.asList(operation1, operation2);

        // Set up the mock behavior
        when(operationService.getAllOperations()).thenReturn(mockOperations);

        // Call the method under test
        List<Operation> result = operationController.getAllOperations();

        // Assert the result
        Assertions.assertEquals(mockOperations.size(), result.size());
        Assertions.assertEquals(mockOperations.get(0).getId(), result.get(0).getId());
        Assertions.assertEquals(mockOperations.get(1).getId(), result.get(1).getId());
        Assertions.assertEquals(mockOperations.get(0).getType(), result.get(0).getType());
        Assertions.assertEquals(mockOperations.get(1).getType(), result.get(1).getType());
        Assertions.assertEquals(mockOperations.get(0).getCost(), result.get(0).getCost());
        Assertions.assertEquals(mockOperations.get(1).getCost(), result.get(1).getCost());
    }


}
