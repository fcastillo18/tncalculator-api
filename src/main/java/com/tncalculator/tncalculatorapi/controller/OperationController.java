package com.tncalculator.tncalculatorapi.controller;

import com.tncalculator.tncalculatorapi.Util.AppUtil;
import com.tncalculator.tncalculatorapi.model.Operation;
import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.payload.request.OperationRequest;
import com.tncalculator.tncalculatorapi.payload.request.RandomStringRequest;
import com.tncalculator.tncalculatorapi.payload.response.OperationResponse;
import com.tncalculator.tncalculatorapi.services.impl.OperationServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/operation")
@Validated
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "2. Operations", description = "The Operations API, for performing basic operations.")
@Order(2)
public class OperationController {

    private final OperationServiceImpl operationServiceImpl;
    private final AppUtil appUtil;

    @Autowired
    public OperationController(OperationServiceImpl operationServiceImpl, AppUtil appUtil) {
        this.operationServiceImpl = operationServiceImpl;
        this.appUtil = appUtil;
    }

    @PostMapping("/subtract")
    @ResponseBody
    public OperationResponse subtract(@RequestBody OperationRequest request) {
        Record record = operationServiceImpl.subtract(request);
        return appUtil.mapRecordToResponse(record);
    }

    @PostMapping("/add")
    @ResponseBody
    public OperationResponse add(@RequestBody OperationRequest request) {
        Record record = operationServiceImpl.add(request);
        return appUtil.mapRecordToResponse(record);
    }

    @PostMapping("/multiply")
    @ResponseBody
    public OperationResponse multiply(@RequestBody OperationRequest request) {
        Record record = operationServiceImpl.multiply(request);
        return appUtil.mapRecordToResponse(record);
    }

    @PostMapping("/divide")
    @ResponseBody
    public OperationResponse divide(@RequestBody OperationRequest request) {
        Record record = operationServiceImpl.divide(request);
        return appUtil.mapRecordToResponse(record);
    }

    @PostMapping("/squareRoot")
    @ResponseBody
    public OperationResponse square(@RequestBody OperationRequest request) {
        Record record = operationServiceImpl.squareRoot(request);
        return appUtil.mapRecordToResponse(record);
    }

    @PostMapping("/randomString")
    @ResponseBody
    public OperationResponse randomString(@RequestBody RandomStringRequest randomStringRequest) {
        OperationRequest request = OperationRequest.builder()
                .userId(NumberUtils.toLong(randomStringRequest.getUserId()))
                .randomString(randomStringRequest.getRandomString().describeConstable())
                .build();
        Record record = operationServiceImpl.randomString(request);
        return appUtil.mapRecordToResponse(record);
    }

    @GetMapping("/all")
    public Page<Operation> getAllOperations(@RequestParam(required = false) Map<String, String> filters,
                                            @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                            @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return operationServiceImpl.getAllOperationsWithFilterAndPagination(filters, page, size);
    }
}
