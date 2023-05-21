package com.tncalculator.tncalculatorapi.aop;

import com.tncalculator.tncalculatorapi.aop.annotation.ValidateUserBalance;
import com.tncalculator.tncalculatorapi.exception.CustomException;
import com.tncalculator.tncalculatorapi.exception.InsufficientBalanceException;
import com.tncalculator.tncalculatorapi.exception.UserNotFoundException;
import com.tncalculator.tncalculatorapi.model.Operation;
import com.tncalculator.tncalculatorapi.model.OperationRequest;
import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.model.User;
import com.tncalculator.tncalculatorapi.repository.OperationRepository;
import com.tncalculator.tncalculatorapi.repository.UserRepository;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;

@Aspect
@Component
public class UserBalanceAspect {

    private final UserRepository userRepository;
    private final OperationRepository operationRepository;

    @Autowired
    public UserBalanceAspect(UserRepository userRepository, OperationRepository operationRepository) {
        this.userRepository = userRepository;
        this.operationRepository = operationRepository;
    }

    @Before("@annotation(validateUserBalance) && args(request)")
    public void validateUserBalance(OperationRequest request, ValidateUserBalance validateUserBalance) {
        Long userId = request.getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        // TODO validate user status (active, inactive) to avoid balance update and throw exception if user is inactive

        BigDecimal userBalance = user.getBalance();
        Operation.OperationType requestOperationType = validateUserBalance.operation();

        if (requestOperationType == null || !Arrays.stream(Operation.OperationType.values()).toList().contains(requestOperationType)) {
            throw new CustomException("Operation not found");
        }
        Operation operation = operationRepository.findByType(requestOperationType);

        if (userBalance.compareTo(operation.getCost()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance");
        }

    }

    @AfterReturning(value = "@annotation(com.tncalculator.tncalculatorapi.aop.annotation.UpdateUserBalance)", returning = "record")
    public void updateUserBalance(Record record) {
        Long userId = record.getUser().getId();
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        BigDecimal userBalance = user.getBalance();
        BigDecimal operationCost = record.getOperation().getCost();

        user.setBalance(userBalance.subtract(operationCost));

        userRepository.save(user);
    }
}
