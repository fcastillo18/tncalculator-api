package com.tncalculator.tncalculatorapi.controller;

import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.services.impl.RecordServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// TODO integration tests (e2e) can be added instead of unit this test to simulate user real actions
@SpringBootTest
public class RecordControllerTests {

    @Mock
    private RecordServiceImpl recordService;

    @InjectMocks
    private RecordController recordController;

    @Test
    public void testGetAllRecords() {
        // Create a sample filters map
        Map<String, String> filters = new HashMap<>();
        filters.put("userId", "1");
        filters.put("operationId", "2");

        // Create a sample page and size
        int page = 0;
        int size = 10;

        // Create a sample page of records
        Pageable pageable = PageRequest.of(page, size, Sort.Direction.DESC, "id");
        @SuppressWarnings("unchecked")
        Page<Record> expectedPage = mock(Page.class);

        // Mock service
        doReturn(pageable).when(expectedPage).getPageable();
        doReturn(1L).when(expectedPage).getTotalElements();
        doReturn(expectedPage.getContent()).when(expectedPage).getContent();
        doReturn(expectedPage).when(recordService).getAllRecordsWithFilterAndPagination(filters, page, size);

        when(recordService.getAllRecordsWithFilterAndPagination(filters, page, size)).thenReturn(expectedPage);

        // Call the method under test
        Page<Record> result = recordController.getAllRecords(filters, page, size);

        // Verify the method calls and assertions
        verify(recordService, times(1)).getAllRecordsWithFilterAndPagination(filters, page, size);
        Assertions.assertEquals(expectedPage, result);
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
