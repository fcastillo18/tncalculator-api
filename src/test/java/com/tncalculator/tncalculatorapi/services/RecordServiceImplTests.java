package com.tncalculator.tncalculatorapi.services;

import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.repository.RecordRepository;
import com.tncalculator.tncalculatorapi.services.impl.RecordServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
public class RecordServiceImplTests {

    @Mock
    private RecordRepository recordRepository;

    @InjectMocks
    private RecordServiceImpl recordService;

    @Test
    public void testGetAllRecords() {
        // Mock data
        Record record1 = new Record();
        record1.setId(1L);
        Record record2 = new Record();
        record2.setId(2L);

        // Mock repository
        when(recordRepository.findAll()).thenReturn(Arrays.asList(record1, record2));

        // Test
        List<Record> records = recordService.getAllRecords();

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

        // Mock repository
        when(recordRepository.save(any(Record.class))).thenReturn(record);

        // Test
        Record result = recordService.createRecord(record);

        // Verify
        Assertions.assertEquals(record, result);
        verify(recordRepository).save(record);
    }

    @Test
    public void testDeleteRecord() {
        // Mock data
        Record record = new Record();
        record.setId(1L);

        // Mock repository
        when(recordRepository.findById(1L)).thenReturn(Optional.of(record));

        // Test
        Assertions.assertDoesNotThrow(() -> recordService.deleteRecord(1L));

        // Verify
        Assertions.assertTrue(record.isDeleted());
        verify(recordRepository).saveAndFlush(record);
    }
}
