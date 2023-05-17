package com.tncalculator.tncalculatorapi.services;

import com.tncalculator.tncalculatorapi.model.Operation;
import com.tncalculator.tncalculatorapi.model.OperationRequest;
import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.repository.OperationRepository;
import com.tncalculator.tncalculatorapi.repository.RecordRepository;
import com.tncalculator.tncalculatorapi.repository.UserRepository;
import com.tncalculator.tncalculatorapi.services.impl.OperationService;
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

    public Record subtract(OperationRequest request) {
        Operation operation = operationRepository.findByType(Operation.OperationType.SUBTRACTION);
        Record record = Record.builder()
                .id(1L)
                .user(userRepository.findById(request.getUserId()).orElseThrow())
                .operation(operation)
                .amount(BigDecimal.valueOf(request.getNum1() - request.getNum2()))
                .userBalance(BigDecimal.valueOf(10))
                .operationResponse("Subtract") // TODO might need an improvement here
                .date(LocalDateTime.now())
                .build();
        recordRepository.save(record);
        return record;
    }

}
