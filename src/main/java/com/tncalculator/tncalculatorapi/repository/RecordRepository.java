package com.tncalculator.tncalculatorapi.repository;

import com.tncalculator.tncalculatorapi.model.Record;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

}
