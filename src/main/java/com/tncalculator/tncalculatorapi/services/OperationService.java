package com.tncalculator.tncalculatorapi.services;

import com.tncalculator.tncalculatorapi.model.Operation;
import com.tncalculator.tncalculatorapi.model.OperationRequest;
import com.tncalculator.tncalculatorapi.model.Record;
import org.springframework.data.domain.Page;

import java.util.Map;


public interface OperationService {

    Record subtract(OperationRequest request);

    Record add(OperationRequest request);

    Record multiply(OperationRequest request);

    Record divide(OperationRequest request);

    Record squareRoot(OperationRequest request);

    Record randomString(OperationRequest request);

    Page<Operation> getAllOperationsWithFilterAndPagination(Map<String, String> filters, int page, int size);
}
