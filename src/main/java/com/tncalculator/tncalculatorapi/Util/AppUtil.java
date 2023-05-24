package com.tncalculator.tncalculatorapi.Util;

import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.payload.response.OperationResponse;
import org.springframework.stereotype.Component;

@Component
public class AppUtil {

    public OperationResponse mapRecordToResponse(Record record) {
        OperationResponse response = new OperationResponse();
        response.setId(record.getId());
        response.setOperationType(record.getOperation().getType().toString());
        response.setOperationCost(record.getOperation().getCost());
        response.setOperationResult(record.getAmount());
        response.setUserRemainingBalance(record.getUser().getBalance());
        return response;
    }
}
