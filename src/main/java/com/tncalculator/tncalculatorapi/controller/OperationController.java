package com.tncalculator.tncalculatorapi.controller;

import com.tncalculator.tncalculatorapi.model.Operation;
import com.tncalculator.tncalculatorapi.model.OperationRequest;
import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.services.impl.OperationServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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

    @PostMapping("/randomString")
    @ResponseBody
    public Record randomString(@RequestBody OperationRequest request) {
        return operationServiceImpl.randomString(request);
    }

    @GetMapping("/all")
    public Page<Operation> getAllOperations(@RequestParam(required = false) Map<String, String> filters,
                                            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return operationServiceImpl.getAllOperationsWithFilterAndPagination(filters, page, size);
    }
}
