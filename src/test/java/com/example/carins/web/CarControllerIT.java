package com.example.carins.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CarControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void listCars_returnsOkAndArray() throws Exception {
        mockMvc.perform(get("/api/cars"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }


    @Test
    void insuranceValid_ok() throws Exception {
        mockMvc.perform(get("/api/cars/1/insurance-valid")
                        .param("date", "2025-06-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carId").value(1))
                .andExpect(jsonPath("$.valid").isBoolean());
    }

    @Test
    void insuranceValid_carNotFound() throws Exception {
        mockMvc.perform(get("/api/cars/999/insurance-valid")
                        .param("date", "2025-06-01"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void insuranceValid_invalidDateFormat() throws Exception {
        mockMvc.perform(get("/api/cars/1/insurance-valid")
                        .param("date", "2025-99-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Date must be in format YYYY-MM-DD"));
    }

    @Test
    void insuranceValid_dateOutOfRange() throws Exception {
        mockMvc.perform(get("/api/cars/1/insurance-valid")
                        .param("date", "2200-01-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Date out of supported range"));
    }


    @Test
    void createClaim_returns201() throws Exception {
        String body = """
                { "claimDate":"2025-01-01",
                  "description":"Scratch on door",
                  "amount":500.0 }
                """;

        mockMvc.perform(post("/api/cars/1/claims")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.description").value("Scratch on door"));
    }

    @Test
    void getClaims_returns200() throws Exception {
        mockMvc.perform(get("/api/cars/1/claims"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON));
    }


    @Test
    void carHistory_returnsOkAndSortedEvents() throws Exception {
        mockMvc.perform(get("/api/cars/1/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carId").value(1))
                .andExpect(jsonPath("$.events").isArray());
    }
}
