package com.example.carins.service.Impl;

import com.example.carins.service.PolicyExpirationLogger;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.repo.InsurancePolicyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDate;
import java.util.List;

@Service
public class PolicyExpirationLoggerImpl implements PolicyExpirationLogger {

    private static final Logger log = LoggerFactory.getLogger(PolicyExpirationLoggerImpl.class);
    private final InsurancePolicyRepository policyRepo;
    private final Clock clock;

    public PolicyExpirationLoggerImpl(InsurancePolicyRepository policyRepo, Clock clock) {
        this.policyRepo = policyRepo;
        this.clock = clock;
    }

    @Override
    @Transactional
    @Scheduled(fixedRate = 60 * 60 * 1000)
    public void logAndMarkExpiredPolicies() {
        LocalDate today = LocalDate.now(clock);
        List<InsurancePolicy> expired = policyRepo.findExpiredUnlogged(today);

        for (InsurancePolicy p : expired) {
            log.info("Policy {} for car {} expired on {}", p.getId(), p.getCar().getId(), p.getEndDate());
            p.setExpiredLogged(true);
        }
    }
}

