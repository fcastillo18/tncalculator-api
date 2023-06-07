package com.tncalculator.tncalculatorapi.services.impl;

import com.tncalculator.tncalculatorapi.model.Record;
import com.tncalculator.tncalculatorapi.repository.RecordRepository;
import com.tncalculator.tncalculatorapi.services.RecordService;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;

    public RecordServiceImpl(RecordRepository recordRepository) {
        this.recordRepository = recordRepository;
    }

    @Override
    public List<Record> getAllRecords() {
        return recordRepository.findAll();
    }

    private Specification<Record> createRecordSpecification(Map<String, String> filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (Map.Entry<String, String> entry : filters.entrySet()) {
                String fieldName = entry.getKey();
                String fieldValue = entry.getValue();
                if (!StringUtils.isEmpty(fieldName) && !StringUtils.isEmpty(fieldValue)) {
                    Path<String> path = root.get(fieldName);
                    predicates.add(criteriaBuilder.equal(path, fieldValue));
                }
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

    @Override
    public Page<Record> getAllRecordsWithFilterAndPagination(Map<String, String> filters, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id"); // Sort by id in descending order
        Pageable pageable = PageRequest.of(page, size, sort);

        // Create a new HashMap to store the modified filters
        Map<String, String> modifiedFilters = new HashMap<>(filters);

        // Remove the "page" and "size" keys from the modified filters
        modifiedFilters.remove("page");
        modifiedFilters.remove("size");

        // If there are any filters, then create a new Specification
        if (!modifiedFilters.isEmpty()) {
            Specification<Record> specification = createRecordSpecification(modifiedFilters);
            return recordRepository.findAll(specification, pageable);
        }

        return recordRepository.findAll(pageable);
    }

    public Page<Record> getAllOperationsByUserIdWithFilterAndPagination(Long userId, Map<String, String> filters, int page, int size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "id"); // Sort by id in descending order
        Pageable pageable = PageRequest.of(page, size, sort);

        // Create a new HashMap to store the modified filters
        Map<String, String> modifiedFilters = new HashMap<>(filters);

        // Remove the "page" and "size" keys from the modified filters
        modifiedFilters.remove("page");
        modifiedFilters.remove("size");

        // If there are any filters, then create a new Specification
        if (!modifiedFilters.isEmpty()) {
            Specification<Record> specification = createRecordSpecification(modifiedFilters);

            // Add additional predicate to filter by user id
            specification = specification.and((root, query, criteriaBuilder) -> {
                Path<Long> userIdPath = root.get("user").get("id");
                return criteriaBuilder.equal(userIdPath, userId);
            });

            return recordRepository.findAll(specification, pageable);
        }

        // Add additional predicate to filter by user id
        Specification<Record> userIdSpecification = (root, query, criteriaBuilder) -> {
            Path<Long> userIdPath = root.get("user").get("id");
            return criteriaBuilder.equal(userIdPath, userId);
        };

        return recordRepository.findAll(userIdSpecification, pageable);
    }


    @Override
    public Record createRecord(Record record) {
        return recordRepository.save(record);
    }

    @Override
    public void deleteRecord(Long id) {
        // Soft delete the record, by setting the isDeleted flag to true
        Record record = recordRepository.findById(id).orElseThrow(() -> new RuntimeException("Record not found"));
        record.setDeleted(true);
        recordRepository.saveAndFlush(record);
    }
}
