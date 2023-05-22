package com.tncalculator.tncalculatorapi.repository;

import com.tncalculator.tncalculatorapi.model.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    Page<Record> findAll(Specification<Record> specification, Pageable pageable);
}
