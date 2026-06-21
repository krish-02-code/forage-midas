package com.jpmc.midascore.service;

import com.jpmc.midascore.entity.TransactionRecord;
import com.jpmc.midascore.entity.UserRecord;
import com.jpmc.midascore.repository.TransactionRecordRepository;
import com.jpmc.midascore.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.jpmc.midascore.foundation.Transaction;

import java.util.Optional;

@Service
public class TransactionService {

    private final UserRepository userRepository;
    private final TransactionRecordRepository transactionRecordRepository;
    private final IncentiveService incentiveService;

    public TransactionService(UserRepository userRepository,
                              TransactionRecordRepository transactionRecordRepository, IncentiveService incentiveService) {
        this.userRepository = userRepository;
        this.transactionRecordRepository = transactionRecordRepository;
        this.incentiveService = incentiveService;
    }

    @Transactional
    public void process(Transaction transaction){
        Optional<UserRecord> senderOpt = Optional.ofNullable(userRepository.findById(transaction.getSenderId()));
        if (senderOpt.isEmpty()) return;

        // Validate recipient exists
        Optional<UserRecord> recipientOpt = Optional.ofNullable(userRepository.findById(transaction.getRecipientId()));
        if (recipientOpt.isEmpty()) return;

        UserRecord sender = senderOpt.get();
        UserRecord recipient = recipientOpt.get();

        // Validate sender has sufficient balance
        if (sender.getBalance() < transaction.getAmount()) return;

        // Get incentive AFTER validation
        float incentiveAmount = incentiveService.getIncentive(transaction);

        // Update balances
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount() + incentiveAmount); // ← incentive added to recipient only

        userRepository.save(sender);
        userRepository.save(recipient);

        transactionRecordRepository.save(
                new TransactionRecord(sender, recipient, transaction.getAmount(), incentiveAmount)
        );
        userRepository.findByName("wilbur").ifPresent(u ->
                System.out.println("wilbur balance: " + u.getBalance())
        );
    }
}