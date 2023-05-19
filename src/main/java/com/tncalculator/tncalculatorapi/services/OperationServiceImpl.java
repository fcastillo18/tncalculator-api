package com.tncalculator.tncalculatorapi.services;

import com.tncalculator.tncalculatorapi.model.Operation;
import com.tncalculator.tncalculatorapi.model.OperationRequest;
import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.model.User;
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
        User user = userRepository.findById(request.getUserId()).orElseThrow();

        if(user.getBalance().compareTo(BigDecimal.ZERO) <= 0){
            throw new RuntimeException("User has no balance to perform this operation");
            // TODO: return a response to the user that the operation was not successful
        }
        // Update the user balance
        user.setBalance(user.getBalance().subtract(operation.getCost())); // cost of the SUBTRACTION operation
        // TODO What if I add Spring AOP to validate if the user got balance before executing the operation,
        //  but also for updating the balance when the endpoint successfully executed?

        Record record = Record.builder()
                .id(1L)
                .user(userRepository.findById(request.getUserId()).orElseThrow())
                .operation(operation)
                .amount(BigDecimal.valueOf(request.getNum1() - request.getNum2()))
                .userBalance(user.getBalance()) // FIXME I dont think I need the user balance on this table, circle back on this later
                .operationResponse("Subtract") // TODO might need an improvement here
                .date(LocalDateTime.now())
                .build();
        recordRepository.save(record);

        // Update the user entity, to persist balance changes
        userRepository.save(user);

        return record;
    }

}
