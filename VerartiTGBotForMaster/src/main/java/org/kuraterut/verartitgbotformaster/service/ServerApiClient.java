package org.kuraterut.verartitgbotformaster.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.ServiceUnavailableException;
import org.kuraterut.verartitgbotformaster.exception.*;
import org.kuraterut.verartitgbotformaster.model.dto.*;
import org.kuraterut.verartitgbotformaster.model.entity.MasterInfo;
import org.kuraterut.verartitgbotformaster.model.entity.Response;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

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

    public void validateToken(String token) throws UnauthorizedException {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token);

            ResponseEntity<Void> response = restTemplate.exchange(
                    apiUrl + "/api/master/token",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    Void.class
            );
        } catch (Exception e) {
            throw new UnauthorizedException("Token is invalid");
        }
    }

    public ScheduleResponse getSchedule(String token, String date) throws UnauthorizedException {
        try {
            // Проверка входных параметров
            if (token == null || token.isBlank()) {
                throw new IllegalArgumentException("Токен авторизации не может быть пустым");
            }
            if (date == null || date.isBlank()) {
                throw new IllegalArgumentException("Дата не может быть пустой");
            }

            // Формирование заголовков
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + token.trim());
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

            // Формирование URL с кодированием параметров
            String encodedDate = URLEncoder.encode(date, StandardCharsets.UTF_8);
            String url = apiUrl + "/api/master/appointment/?date=" + encodedDate;

            // Выполнение запроса
            ResponseEntity<ScheduleResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    ScheduleResponse.class
            );

            // Проверка успешного ответа
            if (response.getStatusCode().is2xxSuccessful() && response.hasBody()) {
                System.out.println("Success");
                return response.getBody();
            } else {
                System.out.println("Runtime");
                throw new RuntimeException("Сервер вернул неожиданный ответ: " + response.getStatusCode());
            }

        } catch (HttpClientErrorException.Unauthorized e) {
            System.out.println("HttpClientErrorException.Unauthorized");
            throw new UnauthorizedException("Требуется повторная авторизация. Токен недействителен или истёк");

        } catch (HttpClientErrorException.Forbidden e) {
            System.out.println("HttpClientErrorException.Forbidden");
            throw new SecurityException("Доступ запрещён. Недостаточно прав для просмотра расписания");

        } catch (HttpClientErrorException.BadRequest e) {
            System.out.println("HttpClientErrorException.BadRequest");
            throw new IllegalArgumentException("Неверный формат даты. Используйте формат dd.MM.yyyy");

        } catch (HttpClientErrorException.NotFound e) {
            System.out.println("HttpClientErrorException.NotFound");
            throw new NotFoundException("Расписание на указанную дату не найдено");

        } catch (ResourceAccessException e) {
            System.out.println("ResourceAccessException");
            throw new ServiceUnavailableException("Сервис расписания временно недоступен. Попробуйте позже");

        } catch (IllegalArgumentException e) {
            System.out.println("IllegalArgumentException");
            throw new IllegalArgumentException("Ошибка в параметрах запроса: " + e.getMessage());

        } catch (RestClientException e) {
            System.out.println("RestClientException");
            throw new RuntimeException("Ошибка при обращении к серверу: " + e.getMessage());

        } catch (Exception e) {
            System.out.println("Exception");
            throw new RuntimeException("Неизвестная ошибка при получении расписания: " + e.getMessage());
        }
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
                apiUrl + "/api/master/profile/",
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