package org.kuraterut.verartitgbotformaster.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {
    private String phone;
    private String password;
    private String role;
}
