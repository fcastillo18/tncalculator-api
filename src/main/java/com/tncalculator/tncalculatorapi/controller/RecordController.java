package com.tncalculator.tncalculatorapi.controller;

import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.services.impl.RecordServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/record")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class RecordController{

    private final RecordServiceImpl recordService;

    @Autowired
    public RecordController(RecordServiceImpl recordService) {
        this.recordService = recordService;
    }

    @GetMapping("/all")
    public Page<Record> getAllRecords(@RequestParam(required = false) Map<String, String> filters,
                                      @RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                      @RequestParam(name = "size", required = false, defaultValue = "10") Integer size) {
            return recordService.getAllRecordsWithFilterAndPagination(filters, page, size);
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
