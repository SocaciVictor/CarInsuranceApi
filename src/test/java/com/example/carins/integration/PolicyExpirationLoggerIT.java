package com.example.carins.integration;

import com.example.carins.model.Car;
import com.example.carins.model.InsurancePolicy;
import com.example.carins.model.Owner;
import com.example.carins.repo.CarRepository;
import com.example.carins.repo.InsurancePolicyRepository;
import com.example.carins.service.Impl.PolicyExpirationLoggerImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.annotation.DirtiesContext;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.core.read.ListAppender;

import java.time.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PolicyExpirationLoggerIT {

    @TestConfiguration
    static class FixedClockConfig {
        @Bean
        Clock clock() {
            return Clock.fixed(Instant.parse("2025-01-01T00:05:00Z"), ZoneOffset.UTC);
        }
    }

    private final InsurancePolicyRepository repo;
    private final CarRepository carRepo;
    private final PolicyExpirationLoggerImpl job;

    PolicyExpirationLoggerIT(InsurancePolicyRepository repo,
                             CarRepository carRepo,
                             PolicyExpirationLoggerImpl job) {
        this.repo = repo;
        this.carRepo = carRepo;
        this.job = job;
    }

    @Test
    void logsOnceAndMarksFlag() {
        var logger = (Logger) LoggerFactory.getLogger(PolicyExpirationLoggerImpl.class);
        var appender = new ListAppender<ch.qos.logback.classic.spi.ILoggingEvent>();
        appender.start();
        logger.addAppender(appender);

        Owner owner = new Owner("Ana", "ana@example.com");
        Car car = new Car("VIN123", "Dacia", "Logan", 2020, owner);
        carRepo.save(car);

        InsurancePolicy pol = new InsurancePolicy(car, "Allianz",
                LocalDate.of(2024, 1, 1),
                LocalDate.of(2024, 12, 31));
        repo.save(pol);

        job.logAndMarkExpiredPolicies();

        Assertions.assertThat(appender.list)
                .anyMatch(e -> e.getFormattedMessage().contains("Policy " + pol.getId()));

        var updated = repo.findById(pol.getId()).orElseThrow();
        Assertions.assertThat(updated.isExpiredLogged()).isTrue();

        appender.list.clear();
        job.logAndMarkExpiredPolicies();
        Assertions.assertThat(appender.list).isEmpty();
    }
}
