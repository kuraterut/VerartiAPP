package org.kuraterut.verartitgbotformaster.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.kuraterut.verartitgbotformaster.exception.AuthException;
import org.kuraterut.verartitgbotformaster.exception.AuthenticationFailedException;
import org.kuraterut.verartitgbotformaster.exception.ServiceUnavailableException;
import org.kuraterut.verartitgbotformaster.model.dto.*;
import org.kuraterut.verartitgbotformaster.exception.BadRequestException;
import org.kuraterut.verartitgbotformaster.model.entity.MasterInfo;
import org.kuraterut.verartitgbotformaster.model.entity.Response;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class ServerApiClient {
    @Value("${server.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public AuthResponse authenticate(String phone, String password) throws AuthException {
        AuthRequest request = new AuthRequest(phone, password, "MASTER");

        try {
            ResponseEntity<AuthResponse> response = restTemplate.postForEntity(
                    apiUrl + "/auth/signin",
                    request,
                    AuthResponse.class
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            throw new AuthException("Неверный телефон или пароль");
        }
    }

    public ScheduleResponse getSchedule(String token, String date) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);

        ResponseEntity<ScheduleResponse> response = restTemplate.exchange(
                apiUrl + "/api/master/appointment?date=" + date,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                ScheduleResponse.class
        );
        return response.getBody();
    }

    public void updateProfileInfo(String token, ProfileInfoRequest request) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        restTemplate.exchange(
                apiUrl + "/api/master/profile/info",
                HttpMethod.PUT,
                new HttpEntity<>(request, headers),
                Void.class
        );
    }

    public void updateProfileBio(String token, String bio) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ProfileBioRequest request = new ProfileBioRequest(bio);

        restTemplate.exchange(
                apiUrl + "/api/master/profile/info",
                HttpMethod.PUT,
                new HttpEntity<>(request, headers),
                Void.class
        );
    }

    public MasterInfo getProfileInfo(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);


        ResponseEntity<MasterInfo> response = restTemplate.exchange(
                apiUrl + "/api/master/profile/info",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                MasterInfo.class
        );
        return response.getBody();
    }

    public void updateProfilePassword(String token, String password) throws Exception{
        String[] passwords = password.split(" ");
        if(passwords.length != 2) throw new Exception("Неверно ввели! Введите старый пароль, затем через пробел новый");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        ChangePasswordRequest request = new ChangePasswordRequest(passwords[0], passwords[1]);

        restTemplate.exchange(
                apiUrl + "/api/master/profile/password",
                HttpMethod.PUT,
                new HttpEntity<>(request, headers),
                Void.class
        );
    }
}