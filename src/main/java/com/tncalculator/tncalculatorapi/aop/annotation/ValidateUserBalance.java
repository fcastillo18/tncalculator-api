package com.tncalculator.tncalculatorapi.aop.annotation;

import com.tncalculator.tncalculatorapi.model.Operation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateUserBalance {
    Operation.OperationType operation();
}
