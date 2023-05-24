package com.tncalculator.tncalculatorapi.services;

import com.tncalculator.tncalculatorapi.TestUtil;
import com.tncalculator.tncalculatorapi.model.Operation;
import com.tncalculator.tncalculatorapi.payload.request.OperationRequest;
import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.model.User;
import com.tncalculator.tncalculatorapi.repository.OperationRepository;
import com.tncalculator.tncalculatorapi.repository.RecordRepository;
import com.tncalculator.tncalculatorapi.repository.UserRepository;
import com.tncalculator.tncalculatorapi.services.impl.OperationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// TODO integration tests (e2e) can be added instead of unit this test to simulate user real actions
@SpringBootTest
public class OperationServiceImplTests {

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecordRepository recordRepository;

    @InjectMocks
    private OperationServiceImpl operationServiceImpl;

    private final TestUtil testUtil = new TestUtil();

    @DisplayName("Method Tests")
    @ParameterizedTest(name = "Testing METHODS, OperationType is {0}")
    @EnumSource(Operation.OperationType.class) // It will iterate all operation, and it will run this same test for all of them
    public void testMethod(Operation.OperationType operationType) {
        long userId = 1L;
        double num1 = 10.0;
        double num2 = 5.0;
        BigDecimal amount = testUtil.performOperation(operationType, BigDecimal.valueOf(num1), BigDecimal.valueOf(num2));
        BigDecimal userBalance = BigDecimal.valueOf(100.0);
        BigDecimal operationCost = BigDecimal.valueOf(10.0);

        // printing some info:
        // TODO can be removed
        System.out.println("value1: "+ num1 + " value2: " + num2);
        System.out.println("OPERATION: "+ operationType);
        System.out.println("Total amount: "+ amount);
        System.out.println("-----------------------------------------");

        // Setting the necessary properties in the operation
        Operation operation = testUtil.getOperation(operationType, operationCost);

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

        Record result = testUtil.getRecordByOperationType(request, operationType, operationServiceImpl);

        // assertions to verify that the result matches the expected record
        Assertions.assertNotNull(result);
        Assertions.assertEquals(expectedRecord.getId(), result.getId());
        Assertions.assertEquals(expectedRecord.getUser(), result.getUser());
        Assertions.assertEquals(expectedRecord.getOperation(), result.getOperation());
    }

}
