package com.tncalculator.tncalculatorapi.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "record")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name="operation_id", nullable=false)
    private Operation operation;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(name = "user_balance", nullable = false)
    private BigDecimal userBalance;

    @Column(name = "operation_response", nullable = false)
    private String operationResponse;

    @Column(nullable = false)
    private LocalDateTime date;
}
