package com.tncalculator.tncalculatorapi.aop;

import com.tncalculator.tncalculatorapi.exception.InsufficientBalanceException;
import com.tncalculator.tncalculatorapi.exception.UserNotFoundException;
import com.tncalculator.tncalculatorapi.model.OperationRequest;
import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.model.User;
import com.tncalculator.tncalculatorapi.repository.UserRepository;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Aspect
@Component
public class UserBalanceAspect {

    private final UserRepository userRepository;

    @Autowired
    public UserBalanceAspect(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Before("@annotation(com.tncalculator.tncalculatorapi.aop.annotation.ValidateUserBalance) && args(request)")
    public void validateUserBalance(OperationRequest request) {
        Long userId = request.getUserId();
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException(userId));

        BigDecimal userBalance = user.getBalance();
        BigDecimal operationAmount = BigDecimal.valueOf(request.getNum1()).subtract(BigDecimal.valueOf(request.getNum2()));

        if (userBalance.compareTo(operationAmount) < 0) {
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
