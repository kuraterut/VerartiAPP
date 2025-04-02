package org.kuraterut.verartitgbotformaster.model.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Getter
@Setter
public class Response {
    HttpStatus status;
    String message;
}
