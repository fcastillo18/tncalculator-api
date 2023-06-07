package com.tncalculator.tncalculatorapi.services;

import com.tncalculator.tncalculatorapi.model.Record;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Map;

public interface RecordService {
    List<Record> getAllRecords();

    Page<Record> getAllRecordsWithFilterAndPagination(Map<String, String> filters, int page, int size);

    Page<Record> getAllOperationsByUserIdWithFilterAndPagination(Long userId, Map<String, String> filters, int page, int size);

    Record createRecord(Record record);

    void deleteRecord(Long id);
}
