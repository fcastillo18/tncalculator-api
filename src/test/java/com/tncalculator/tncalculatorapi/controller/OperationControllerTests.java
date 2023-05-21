package com.tncalculator.tncalculatorapi.controller;

import com.tncalculator.tncalculatorapi.model.Operation;
import com.tncalculator.tncalculatorapi.model.OperationRequest;
import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.model.User;
import com.tncalculator.tncalculatorapi.repository.OperationRepository;
import com.tncalculator.tncalculatorapi.repository.RecordRepository;
import com.tncalculator.tncalculatorapi.repository.UserRepository;
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
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class OperationControllerTests {
    @Mock
    private OperationServiceImpl operationService;

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecordRepository recordRepository;

    @InjectMocks
    private OperationController operationController;

    @InjectMocks
    private OperationServiceImpl operationServiceImpl;

    private Operation getOperation(Operation.OperationType operationType, BigDecimal operationCost){
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

    private Record getRecordByOperationType(OperationRequest request, Operation.OperationType operationType) {

        return switch (operationType) {
            case ADDITION -> operationServiceImpl.add(request);
            case SUBTRACTION -> operationServiceImpl.subtract(request);
            case MULTIPLICATION -> operationServiceImpl.multiply(request);
            case DIVISION -> operationServiceImpl.divide(request);
            case SQUARE_ROOT -> operationServiceImpl.squareRoot(request);
            case RANDOM_STRING -> operationServiceImpl.randomString(request);
        };
    }


    @DisplayName("Endpoint Tests")
    @ParameterizedTest(name = "Testing ENDPOINTS, OperationType is {0}")
    @EnumSource(Operation.OperationType.class) // It will iterate all operation, and it will run this same test for all of them
    public void testEndpoint(Operation.OperationType operationType) {
        System.out.println("OPERATION: "+ operationType);
        long userId = 1L;
        double num1 = 10.0;
        double num2 = 5.0;
        BigDecimal amount = performOperation(operationType, BigDecimal.valueOf(num1), BigDecimal.valueOf(num2));
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
        Operation operation = getOperation(operationType, operationCost);

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

    @DisplayName("Method Tests")
    @ParameterizedTest(name = "Testing METHODS, OperationType is {0}")
    @EnumSource(Operation.OperationType.class) // It will iterate all operation, and it will run this same test for all of them
    public void testMethod(Operation.OperationType operationType) {
        long userId = 1L;
        double num1 = 10.0;
        double num2 = 5.0;
        BigDecimal amount = performOperation(operationType, BigDecimal.valueOf(num1), BigDecimal.valueOf(num2));
        BigDecimal userBalance = BigDecimal.valueOf(100.0);
        BigDecimal operationCost = BigDecimal.valueOf(10.0);

        // printing some info:
        // TODO can be removed
        System.out.println("value1: "+ num1 + " value2: " + num2);
        System.out.println("OPERATION: "+ operationType);
        System.out.println("Total amount: "+ amount);
        System.out.println("-----------------------------------------");

        // Setting the necessary properties in the operation
        Operation operation = getOperation(operationType, operationCost);

        // Setting the necessary properties in the user
        User user = User.builder()
                .id(userId)
                .balance(userBalance)
                .build();

        // Setting the necessary properties in the request
        OperationRequest request = new OperationRequest(userId, num1, num2);

        // Setting the expected properties in the record
        Record expectedRecord = Record.builder()
                .user(user)
                .operation(operation)
                .amount(amount)
                .build();


        when(operationRepository.findByType(operationType)).thenReturn(operation);
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(user));
        when(recordRepository.save(any(Record.class))).thenReturn(expectedRecord);

        Record result = getRecordByOperationType(request, operationType);

        // assertions to verify that the result matches the expected record
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedRecord.getId(), result.getId());
        Assertions.assertEquals(expectedRecord.getUser(), result.getUser());
        Assertions.assertEquals(expectedRecord.getOperation(), result.getOperation());
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
