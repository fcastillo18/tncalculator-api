package com.tncalculator.tncalculatorapi.controller;

import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.services.impl.RecordServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/record")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "3. OperationRecord", description = "The OperationRecord API. Contains all the operations that can be performed on a record.")
@Order(3)
public class RecordController{

    private final RecordServiceImpl recordService;

    @Autowired
    public RecordController(RecordServiceImpl recordService) {
        this.recordService = recordService;
    }

    // TODO I need to return a custom response and get rid of some fields like the user pass.
    @GetMapping("/all")
    public Page<Record> getAllRecords(@RequestParam(required = false) Map<String, String> filters,
                                      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                      @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
            return recordService.getAllRecordsWithFilterAndPagination(filters, page, size);
    }

    @GetMapping("/all/{userId}")
    public Page<Record> getAllOperationsByUserIdWithFilterAndPagination(@PathVariable Long userId,
                                                                        @RequestParam(required = false) Map<String, String> filters,
                                                                        @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                                                        @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
        return recordService.getAllOperationsByUserIdWithFilterAndPagination(userId, filters, page, size);
    }

    @PostMapping("/create")
    public Record createRecord(@RequestBody Record record) {
        return recordService.createRecord(record);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRecord(@PathVariable Long id) {
        recordService.deleteRecord(id);
    }
}
