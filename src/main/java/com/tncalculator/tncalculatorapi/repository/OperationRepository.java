package com.tncalculator.tncalculatorapi.repository;

import com.tncalculator.tncalculatorapi.model.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OperationRepository extends JpaRepository<Operation, Long> {
    Operation findByType(Operation.OperationType type);

    Page<Operation> findAll(Specification<Operation> specification, Pageable pageable);
}
