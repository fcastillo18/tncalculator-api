package com.tncalculator.tncalculatorapi.controller;

import com.tncalculator.tncalculatorapi.model.OperationRequest;
import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.services.OperationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/operation")
public class OperationController {

    private final OperationServiceImpl operationServiceImpl;

    @Autowired
    public OperationController(OperationServiceImpl operationServiceImpl) {
        this.operationServiceImpl = operationServiceImpl;
    }

    @GetMapping("/hello")
    @ResponseBody
    public String helloWorld() {
        return "Hello World";
    }

    @PostMapping("/subtract")
    @ResponseBody
    public Record subtract(@RequestBody OperationRequest request) {
        System.out.println(request);
        return operationServiceImpl.subtract(request);
    }
}
