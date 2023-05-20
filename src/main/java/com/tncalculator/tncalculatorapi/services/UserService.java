package com.tncalculator.tncalculatorapi.services;

import com.tncalculator.tncalculatorapi.model.Operation;
import com.tncalculator.tncalculatorapi.model.User;

public interface UserService {

    public boolean hasSufficientBalance(User user, Operation operation);
}
