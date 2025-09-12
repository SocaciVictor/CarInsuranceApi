package com.example.carins.service;

import com.example.carins.web.dto.CarHistoryResponse;

public interface CarHistoryService {
    CarHistoryResponse getHistory(Long carId);
}
