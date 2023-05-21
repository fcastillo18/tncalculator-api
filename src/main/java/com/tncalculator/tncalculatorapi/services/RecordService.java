package com.tncalculator.tncalculatorapi.services;

import com.tncalculator.tncalculatorapi.model.Record;

import java.util.List;

public interface RecordService {
    List<Record> getAllRecords();
    Record createRecord(Record record);
    void deleteRecord(Long id);
}
