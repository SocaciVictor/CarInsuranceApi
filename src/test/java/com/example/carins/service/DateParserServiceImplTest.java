package com.example.carins.service;

import com.example.carins.Exception.InvalidDateException;
import com.example.carins.service.Impl.DateParserServiceImpl;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;


class DateParserServiceImplTest {

    DateParserServiceImpl service = new DateParserServiceImpl();

    @Test
    void parse_validDate() {
        assertThat(service.parseAndValidate("2024-01-01")).isEqualTo(LocalDate.of(2024,1,1));
    }

    @Test
    void parse_invalidFormat() {
        assertThrows(InvalidDateException.class, () -> service.parseAndValidate("2024/01/01"));
    }

    @Test
    void parse_outOfRange() {
        assertThrows(InvalidDateException.class, () -> service.parseAndValidate("1800-01-01"));
    }
}
