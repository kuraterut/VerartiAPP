package org.kuraterut.verartitgbotformaster.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfileInfoRequest {
    private String surname;
    private String name;
    private String patronymic;
}