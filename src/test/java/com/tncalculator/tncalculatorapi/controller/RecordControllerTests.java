package com.tncalculator.tncalculatorapi.controller;

import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.services.impl.RecordServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RecordControllerTests {

    @Mock
    private RecordServiceImpl recordService;

    @InjectMocks
    private RecordController recordController;

    @Test
    public void testGetAllRecords() {
        // Mock data
        Record record1 = new Record();
        record1.setId(1L);
        Record record2 = new Record();
        record2.setId(2L);

        // Mock service
        when(recordService.getAllRecords()).thenReturn(Arrays.asList(record1, record2));

        // Test
        List<Record> records = recordController.getAllRecords();

        // Verify
        Assertions.assertEquals(2, records.size());
        Assertions.assertEquals(record1, records.get(0));
        Assertions.assertEquals(record2, records.get(1));
    }

    @Test
    public void testCreateRecord() {
        // Mock data
        Record record = new Record();
        record.setId(1L);

        // Mock service
        when(recordService.createRecord(any(Record.class))).thenReturn(record);

        // Test
        Record result = recordController.createRecord(record);

        // Verify
        Assertions.assertEquals(record, result);
        verify(recordService).createRecord(record);
    }

    @Test
    public void testDeleteRecord() {
        // Test
        Assertions.assertDoesNotThrow(() -> recordController.deleteRecord(1L));

        // Verify
        verify(recordService).deleteRecord(1L);
    }
}
