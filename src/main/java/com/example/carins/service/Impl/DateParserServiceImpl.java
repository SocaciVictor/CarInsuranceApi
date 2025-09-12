package com.example.carins.service.Impl;

import com.example.carins.Exception.InvalidDateException;
import com.example.carins.service.DateParserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class DateParserServiceImpl implements DateParserService {

    public LocalDate parseAndValidate(String raw) {
        LocalDate date;
        try {
            date = LocalDate.parse(raw);
        } catch (Exception e) {
            throw new InvalidDateException("Date must be in format YYYY-MM-DD");
        }
        if (date.isBefore(LocalDate.of(1900,1,1)) || date.isAfter(LocalDate.of(2100,12,31))) {
            throw new InvalidDateException("Date out of supported range");
        }
        return date;
    }
}
