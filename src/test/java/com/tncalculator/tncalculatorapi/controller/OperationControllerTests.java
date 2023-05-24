package com.tncalculator.tncalculatorapi.controller;

import com.tncalculator.tncalculatorapi.TestUtil;
import com.tncalculator.tncalculatorapi.model.Operation;
import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.model.User;
import com.tncalculator.tncalculatorapi.payload.request.OperationRequest;
import com.tncalculator.tncalculatorapi.services.impl.OperationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

// TODO integration tests (e2e) can be added instead of unit this test to simulate user real actions
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
//        when(operationService.subtract(any(OperationRequest.class))).thenReturn(expectedRecord);
        testUtil.mockOperationByOperationType(operationType, operationService, expectedRecord);
        Record result = testUtil.getRecordByOperationType(request, operationType, operationService);

        // assertions to verify that the result matches the expected record
        Assertions.assertEquals(expectedRecord.getId(), result.getId());
        Assertions.assertEquals(expectedRecord.getUser(), result.getUser());
        Assertions.assertEquals(expectedRecord.getOperation(), result.getOperation());
        Assertions.assertEquals(expectedRecord.getAmount(), result.getAmount());
    }

    @Test
    public void testGetAllOperations() {
        // Create a sample filters map
        Map<String, String> filters = new HashMap<>();
        filters.put("id", "1");
        filters.put("type", "ADDITION");

        // Create a sample page and size
        int page = 0;
        int size = 10;

        // Create a sample page of records
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");
        @SuppressWarnings("unchecked")
        Page<Operation> expectedPage = mock(Page.class);

        // Mock service
        doReturn(pageable).when(expectedPage).getPageable();
        doReturn(1L).when(expectedPage).getTotalElements();
        doReturn(expectedPage.getContent()).when(expectedPage).getContent();
        doReturn(expectedPage).when(operationService).getAllOperationsWithFilterAndPagination(filters, page, size);

        when(operationService.getAllOperationsWithFilterAndPagination(filters, page, size)).thenReturn(expectedPage);

        // Call the method under test
        Page<Operation> result = operationService.getAllOperationsWithFilterAndPagination(filters, page, size);

        // Verify the method calls and assertions
        verify(operationService, times(1)).getAllOperationsWithFilterAndPagination(filters, page, size);
        Assertions.assertEquals(expectedPage, result);
    }

}
