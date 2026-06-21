package com.jpmc.midascore.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "transaction_records")
@Data
@AllArgsConstructor
public class TransactionRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private UserRecord sender;

    @ManyToOne
    @JoinColumn(name = "recipient_id")
    private UserRecord recipient;

    private float amount;
    private float incentive;

    public TransactionRecord() {}

    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
    }

    public TransactionRecord(UserRecord sender, UserRecord recipient, float amount, float incentiveAmount){
        this.amount = amount;
        this.recipient = recipient;
        this.sender = sender;
        this.incentive = incentiveAmount;
    }
}