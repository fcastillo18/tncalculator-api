package com.tncalculator.tncalculatorapi.controller;

import com.tncalculator.tncalculatorapi.model.OperationRequest;
import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.services.impl.OperationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/operation")
@Validated
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
        return operationServiceImpl.subtract(request);
    }

    @PostMapping("/add")
    @ResponseBody
    public Record add(@RequestBody OperationRequest request) {
        return operationServiceImpl.add(request);
    }

    @PostMapping("/multiply")
    @ResponseBody
    public Record multiply(@RequestBody OperationRequest request) {
        return operationServiceImpl.multiply(request);
    }

    @PostMapping("/divide")
    @ResponseBody
    public Record divide(@RequestBody OperationRequest request) {
        return operationServiceImpl.divide(request);
    }

    @PostMapping("/squareRoot")
    @ResponseBody
    public Record square(@RequestBody OperationRequest request) {
        return operationServiceImpl.squareRoot(request);
    }
}
