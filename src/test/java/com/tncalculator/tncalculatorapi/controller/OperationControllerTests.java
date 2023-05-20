package com.tncalculator.tncalculatorapi.controller;

import com.tncalculator.tncalculatorapi.model.Operation;
import com.tncalculator.tncalculatorapi.model.OperationRequest;
import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.model.User;
import com.tncalculator.tncalculatorapi.repository.OperationRepository;
import com.tncalculator.tncalculatorapi.repository.RecordRepository;
import com.tncalculator.tncalculatorapi.repository.UserRepository;
import com.tncalculator.tncalculatorapi.services.impl.OperationServiceImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// TODO refactor this class to reuse a bunch of code and reduce the size of the class.
// TODO Try to create simple test that with customize parameters
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

}
