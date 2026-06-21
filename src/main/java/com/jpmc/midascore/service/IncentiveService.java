package com.jpmc.midascore.service;
import com.jpmc.midascore.entity.Incentive;
import com.jpmc.midascore.foundation.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class IncentiveService {

    private final RestTemplate restTemplate;

    public IncentiveService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public float getIncentive(Transaction transaction) {
        Incentive incentive = restTemplate.postForObject(
            "http://localhost:8080/incentive",
            transaction,
            Incentive.class
        );
        return incentive != null ? incentive.getAmount() : 0f;
    }
}