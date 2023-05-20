package com.tncalculator.tncalculatorapi.services.impl;

import com.tncalculator.tncalculatorapi.aop.annotation.UpdateUserBalance;
import com.tncalculator.tncalculatorapi.aop.annotation.ValidateUserBalance;
import com.tncalculator.tncalculatorapi.exception.CustomException;
import com.tncalculator.tncalculatorapi.model.Operation;
import com.tncalculator.tncalculatorapi.model.OperationRequest;
import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.model.User;
import com.tncalculator.tncalculatorapi.repository.OperationRepository;
import com.tncalculator.tncalculatorapi.repository.RecordRepository;
import com.tncalculator.tncalculatorapi.repository.UserRepository;
import com.tncalculator.tncalculatorapi.services.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class OperationServiceImpl implements OperationService {

    private final UserRepository userRepository;
    private final RecordRepository recordRepository;
    private final OperationRepository operationRepository;

    @Autowired
    public OperationServiceImpl(UserRepository userRepository, RecordRepository recordRepository, OperationRepository operationRepository) {
        this.userRepository = userRepository;
        this.recordRepository = recordRepository;
        this.operationRepository = operationRepository;
    }

    // Utility method to create a record and save it to the database.
    private Record createRecord(OperationRequest request, Operation.OperationType operationType) {
        Operation operation = operationRepository.findByType(operationType);
        User user = userRepository.findById(request.getUserId()).orElseThrow();

        BigDecimal amount;

        if (operationType == Operation.OperationType.ADDITION) {
            amount = BigDecimal.valueOf(request.getNum1() + request.getNum2());
        } else if (operationType == Operation.OperationType.SUBTRACTION) {
            amount = BigDecimal.valueOf(request.getNum1() - request.getNum2());
        } else if (operationType == Operation.OperationType.MULTIPLICATION) {
            amount = BigDecimal.valueOf(request.getNum1() * request.getNum2());
        } else if (operationType == Operation.OperationType.DIVISION) {
            // Handle division by zero case
            if (request.getNum2() == 0) {
                throw new CustomException("Division by zero is not allowed");
            }
            amount = BigDecimal.valueOf(request.getNum1() / request.getNum2());
        } else {
            // Handle other operation types if needed
            throw new CustomException("Unsupported operation type: " + operationType);
        }

        Record record = Record.builder()
                .user(user)
                .operation(operation)
                .amount(amount)
                .operationResponse(operationType.toString())
                .date(LocalDateTime.now())
                .build();

        recordRepository.save(record);
        return record;
    }

    // user balance tracking for each request each manage by these two annotations and using AOP
    @Override
    @ValidateUserBalance(operation = Operation.OperationType.SUBTRACTION)
    @UpdateUserBalance
    public Record subtract(OperationRequest request) {
        return createRecord(request, Operation.OperationType.SUBTRACTION);
    }

    @Override
    @ValidateUserBalance(operation = Operation.OperationType.ADDITION)
    @UpdateUserBalance
    public Record add(OperationRequest request) {
        return createRecord(request, Operation.OperationType.ADDITION);
    }

    @Override
    @ValidateUserBalance(operation = Operation.OperationType.ADDITION)
    @UpdateUserBalance
    public Record multiply(OperationRequest request) {
        return createRecord(request, Operation.OperationType.MULTIPLICATION);
    }

    @Override
    @ValidateUserBalance(operation = Operation.OperationType.DIVISION)
    @UpdateUserBalance
    public Record divide(OperationRequest request) {
        return createRecord(request, Operation.OperationType.DIVISION);
    }

}
