package com.tncalculator.tncalculatorapi.services;

import com.tncalculator.tncalculatorapi.model.OperationRequest;
import com.tncalculator.tncalculatorapi.model.Record;


public interface OperationService {

    Record subtract(OperationRequest request);

    Record add(OperationRequest request);

    Record multiply(OperationRequest request);

    Record divide(OperationRequest request);

    Record squareRoot(OperationRequest request);

    Record randomString(OperationRequest request);
}
