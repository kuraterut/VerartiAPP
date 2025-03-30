package org.admin.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse extends Response {
    String authToken;
}
