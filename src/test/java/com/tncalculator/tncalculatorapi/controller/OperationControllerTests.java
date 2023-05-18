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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.Optional;

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

    @Test
    public void testSubtractEndpoint() {
        OperationRequest request = new OperationRequest();
        // Set the necessary properties in the request

        Record expectedRecord = new Record();
        // Set the expected properties in the record

        when(operationService.subtract(any(OperationRequest.class))).thenReturn(expectedRecord);

        Record result = operationController.subtract(request);

        // assertions to verify that the result matches the expected record
        Assertions.assertEquals(expectedRecord.getId(), result.getId());
        Assertions.assertEquals(expectedRecord.getUser(), result.getUser());
        Assertions.assertEquals(expectedRecord.getOperation(), result.getOperation());
    }

    @Test
    public void testSubtractMethod() {
        OperationRequest request = new OperationRequest();
        // Set the necessary properties in the request

        Operation operation = new Operation();
        // Set the necessary properties in the operation

        User user = new User();
        // Set the necessary properties in the user

        Record expectedRecord = new Record();
        // Set the expected properties in the record

        when(operationRepository.findByType(Operation.OperationType.SUBTRACTION)).thenReturn(operation);
        when(userRepository.findById(request.getUserId())).thenReturn(Optional.of(user));
        when(recordRepository.save(any(Record.class))).thenReturn(expectedRecord);

        Record result = operationServiceImpl.subtract(request);

        // assertions to verify that the result matches the expected record
        Assertions.assertEquals(expectedRecord.getId(), result.getId());
        Assertions.assertEquals(expectedRecord.getUser(), result.getUser());
        Assertions.assertEquals(expectedRecord.getOperation(), result.getOperation());
    }
}
