package org.kuraterut.verartitgbotformaster.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    @JsonProperty("old_password")
    String oldPassword;
    @JsonProperty("new_password")
    String newPassword;
}
