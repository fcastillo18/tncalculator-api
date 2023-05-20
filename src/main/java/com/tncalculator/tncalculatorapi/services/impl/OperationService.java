package com.tncalculator.tncalculatorapi.services.impl;

import com.tncalculator.tncalculatorapi.model.OperationRequest;
import com.tncalculator.tncalculatorapi.model.Record;


public interface OperationService {

    Record subtract(OperationRequest request);
    Record add(OperationRequest request);
    Record multiply(OperationRequest request);
}
