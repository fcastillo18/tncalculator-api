package com.tncalculator.tncalculatorapi.controller;

import com.tncalculator.tncalculatorapi.model.Operation;
import com.tncalculator.tncalculatorapi.model.OperationRequest;
import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.model.User;
import com.tncalculator.tncalculatorapi.repository.OperationRepository;
import com.tncalculator.tncalculatorapi.repository.RecordRepository;
import com.tncalculator.tncalculatorapi.repository.UserRepository;
import com.tncalculator.tncalculatorapi.services.OperationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// TODO refactor this class to reuse a bunch of code and reduce the size of the class
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
                .id(Operation.OperationType.valueOf(operationType.name()).ordinal() + 1L) // brutal force to get the "operationId"
                .type(operationType)
                .cost((operationCost))
                .build();
    }

    @Nested
    @DisplayName("Subtract Tests")
    class SubtractTests{
        @Test
        @DisplayName("Subtract Endpoint Test")
        public void testSubtractEndpoint() {
            Long userId = 1L;
            double num1 = 10.0;
            double num2 = 5.0;
            BigDecimal amount = BigDecimal.valueOf(num1 - num2);
            BigDecimal userBalance = BigDecimal.valueOf(100.0);
            BigDecimal operationCost = BigDecimal.valueOf(10.0);

            // Setting the necessary properties in the user
            User user = User.builder()
                    .id(userId)
                    .balance(userBalance)
                    .build();

            // Setting the necessary properties in the request
            OperationRequest request = OperationRequest.builder()
                    .userId(user.getId())
                    .num1(num1)
                    .num1(num2)
                    .build();

            // Setting the necessary properties in the operation
            Operation operation = getOperation(Operation.OperationType.SUBTRACTION, operationCost);


            // Setting the expected properties in the record
            Record expectedRecord = Record.builder()
                    .id(1L)
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
        @DisplayName("Subtract Method Test")
        public void testSubtractMethod() {

            Long userId = 1L;
            double num1 = 10.0;
            double num2 = 5.0;
            BigDecimal amount = BigDecimal.valueOf(num1 - num2);
            BigDecimal userBalance = BigDecimal.valueOf(100.0);
            BigDecimal operationCost = BigDecimal.valueOf(10.0);

            // Setting the necessary properties in the operation
            Operation operation = getOperation(Operation.OperationType.SUBTRACTION, operationCost);

            // Setting the necessary properties in the user
            User user = User.builder()
                    .id(userId)
                    .balance(userBalance)
                    .build();

            // Setting the necessary properties in the request
            OperationRequest request = OperationRequest.builder()
                    .userId(userId)
                    .num1(num1)
                    .num1(num2)
                    .build();

            // Setting the expected properties in the record
            Record expectedRecord = Record.builder()
                    .id(1L)
                    .user(user)
                    .operation(operation)
                    .amount(amount)
                    .build();


            when(operationRepository.findByType(Operation.OperationType.SUBTRACTION)).thenReturn(operation);
            when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(user));
            when(recordRepository.save(any(Record.class))).thenReturn(expectedRecord);
            // Specify the behavior of the subtract method to return the mock record
            when(operationServiceImpl.subtract(request)).thenReturn(expectedRecord);

            Record result = operationServiceImpl.subtract(request);

            // assertions to verify that the result matches the expected record
            Assertions.assertNotNull(result);
            Assertions.assertEquals(expectedRecord.getId(), result.getId());
            Assertions.assertEquals(expectedRecord.getUser(), result.getUser());
            Assertions.assertEquals(expectedRecord.getOperation(), result.getOperation());
        }
    }

    @Nested
    @DisplayName("Add Tests")
    class AddTests{
        @Test
        @DisplayName("Add Endpoint Test")
        public void testAddEndpoint() {
            Long userId = 1L;
            double num1 = 10.0;
            double num2 = 5.0;
            BigDecimal amount = BigDecimal.valueOf(num1 - num2);
            BigDecimal userBalance = BigDecimal.valueOf(100.0);
            BigDecimal operationCost = BigDecimal.valueOf(10.0);

            // Setting the necessary properties in the user
            User user = User.builder()
                    .id(userId)
                    .balance(userBalance)
                    .build();

            // Setting the necessary properties in the request
            OperationRequest request = OperationRequest.builder()
                    .userId(user.getId())
                    .num1(num1)
                    .num1(num2)
                    .build();

            // Setting the necessary properties in the operation
            Operation operation = getOperation(Operation.OperationType.ADDITION, operationCost);

            // Setting the expected properties in the record
            Record expectedRecord = Record.builder()
                    .id(1L)
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
        @DisplayName("Add Method Test")
        public void testAddMethod() {

            Long userId = 1L;
            double num1 = 10.0;
            double num2 = 5.0;
            BigDecimal amount = BigDecimal.valueOf(num1 - num2);
            BigDecimal userBalance = BigDecimal.valueOf(100.0);
            BigDecimal operationCost = BigDecimal.valueOf(10.0);

            // Setting the necessary properties in the operation
            Operation operation = getOperation(Operation.OperationType.ADDITION, operationCost);

            // Setting the necessary properties in the user
            User user = User.builder()
                    .id(userId)
                    .balance(userBalance)
                    .build();

            // Setting the necessary properties in the request
            OperationRequest request = OperationRequest.builder()
                    .userId(userId)
                    .num1(num1)
                    .num1(num2)
                    .build();

            // Setting the expected properties in the record
            Record expectedRecord = Record.builder()
                    .id(1L)
                    .user(user)
                    .operation(operation)
                    .amount(amount)
                    .build();


            when(operationRepository.findByType(Operation.OperationType.ADDITION)).thenReturn(operation);
            when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(user));
            when(recordRepository.save(any(Record.class))).thenReturn(expectedRecord);
            // Specify the behavior of the subtract method to return the mock record
            when(operationServiceImpl.add(request)).thenReturn(expectedRecord);

            Record result = operationServiceImpl.add(request);

            // assertions to verify that the result matches the expected record
            Assertions.assertNotNull(result);
            Assertions.assertEquals(expectedRecord.getId(), result.getId());
            Assertions.assertEquals(expectedRecord.getUser(), result.getUser());
            Assertions.assertEquals(expectedRecord.getOperation(), result.getOperation());
        }
    }

}
