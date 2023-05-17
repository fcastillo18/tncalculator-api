package com.tncalculator.tncalculatorapi.services.impl;

import com.tncalculator.tncalculatorapi.model.OperationRequest;
import com.tncalculator.tncalculatorapi.model.Record;


public interface OperationService {

    public Record subtract(OperationRequest request);

}
