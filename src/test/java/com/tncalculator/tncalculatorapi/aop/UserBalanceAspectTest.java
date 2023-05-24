package com.tncalculator.tncalculatorapi.aop;

import com.tncalculator.tncalculatorapi.aop.annotation.ValidateUserBalance;
import com.tncalculator.tncalculatorapi.exception.InsufficientBalanceException;
import com.tncalculator.tncalculatorapi.model.Operation;
import com.tncalculator.tncalculatorapi.payload.request.OperationRequest;
import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.model.User;
import com.tncalculator.tncalculatorapi.repository.OperationRepository;
import com.tncalculator.tncalculatorapi.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserBalanceAspectTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private OperationRepository operationRepository;

    @InjectMocks
    private UserBalanceAspect userBalanceAspect;

    private final Operation.OperationType ADDITION_TYPE = Operation.OperationType.ADDITION;

    // Custom method to create a ValidateUserBalance annotation with the given operation type
    private ValidateUserBalance createValidateUserBalance(Operation.OperationType operationType) {
        return new ValidateUserBalance() {
            @Override
            public Operation.OperationType operation() {
                return operationType;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return ValidateUserBalance.class;
            }
        };
    }

    @Test
    public void testValidateUserBalance_WithSufficientBalance() {
        // Prepare
        Operation.OperationType operationType = Operation.OperationType.ADDITION;
        OperationRequest request = new OperationRequest(1L, 10, 5);

        User user = new User();
        user.setBalance(BigDecimal.valueOf(15));
        Operation operation = new Operation(1L, operationType, BigDecimal.valueOf(10));
        operation.setCost(BigDecimal.valueOf(10));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(operationRepository.findByType(operationType)).thenReturn(operation);

        // validateUserBalance method is annotated with ValidateUserBalance annotation,  so we can use it here to test the aspect
        ValidateUserBalance validateUserBalance = createValidateUserBalance(operationType);

        // Execute and Verify - No exception should be thrown
        Assertions.assertDoesNotThrow(() ->
                userBalanceAspect.validateUserBalance(request, validateUserBalance),
                "No exception should be thrown");
    }



    @Test
    public void testValidateUserBalance_WithInsufficientBalance() {
        // Prepare
        Operation.OperationType operationType = Operation.OperationType.ADDITION;
        OperationRequest request = new OperationRequest(1L, 10, 5) ;
        User user = new User();
        user.setBalance(BigDecimal.valueOf(0)); // zero balance
        Operation operation = new Operation(1L, ADDITION_TYPE,BigDecimal.valueOf(10));
        operation.setCost(BigDecimal.valueOf(10));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(operationRepository.findByType(ADDITION_TYPE)).thenReturn(operation);

        // validateUserBalance method is annotated with ValidateUserBalance annotation,  so we can use it here to test the aspect
        ValidateUserBalance validateUserBalance = createValidateUserBalance(operationType);

        // Execute and Verify
        Assertions.assertThrows(InsufficientBalanceException.class, () -> userBalanceAspect.validateUserBalance(request, validateUserBalance));
    }

    @Test
    public void testUpdateUserBalance() {
        BigDecimal balance = BigDecimal.valueOf(15);
        BigDecimal operationCost = BigDecimal.valueOf(10);

        // Prepare
        User user = new User();
        user.setId(1L);
        user.setBalance(balance);

        Operation operation = new Operation();
        operation.setCost(operationCost);

        BigDecimal updatedBalance = balance.subtract(operationCost);

        Record record = new Record();
        record.setAmount(updatedBalance);
        record.setUser(user);
        record.setOperation(operation);
        record.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Execute
        userBalanceAspect.updateUserBalance(record);

        // Verify
        verify(userRepository).save(user);
        Assertions.assertEquals(updatedBalance, user.getBalance());
    }
}
