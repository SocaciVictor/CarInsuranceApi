package com.example.carins.service;

import java.time.LocalDate;

public interface DateParserService {
    LocalDate parseAndValidate(String raw);
}
