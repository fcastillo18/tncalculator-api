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
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
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

    @Mock
    private OperationController operationServiceImpl;

    // TODO I could use a little bit of refactor with the different objects I'm using here

    @Test
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
        Operation operation = Operation.builder()
                .type(Operation.OperationType.SUBTRACTION)
                .cost(operationCost)
                .build();

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
    public void testSubtractMethod() {

        Long userId = 1L;
        double num1 = 10.0;
        double num2 = 5.0;
        BigDecimal amount = BigDecimal.valueOf(num1 - num2);
        BigDecimal userBalance = BigDecimal.valueOf(100.0);
        BigDecimal operationCost = BigDecimal.valueOf(10.0);

        // Setting the necessary properties in the operation
        Operation operation = Operation.builder()
                .type(Operation.OperationType.SUBTRACTION)
                .cost(operationCost)
                .build();

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
