package org.kuraterut.verartitgbotformaster.service;

import org.kuraterut.verartitgbotformaster.model.dto.AuthRequest;
import org.kuraterut.verartitgbotformaster.model.dto.AuthResponse;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ApiService {
    private final RestTemplate restTemplate;
    private final String apiUrl = "http://localhost:8000";

    public ApiService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public AuthResponse authenticate(String phone, String password) {
        AuthRequest request = new AuthRequest();
        request.setPhone(phone);
        request.setPassword(password);
        request.setRole("MASTER");

        ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                apiUrl + "/auth/signin",
                request,
                AuthResponse.class
        );

        return response.getBody();
    }

    public String getSchedule(String token, String date) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                apiUrl + "/api/master/appointment?date=" + date,
                HttpMethod.GET,
                entity,
                String.class
        );

        return response.getBody();
    }
}