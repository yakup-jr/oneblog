package net.oneblog.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The type Login request dto.
 */
@Data
@AllArgsConstructor
public class LoginRequestDto {

    private String username;
    private String password;

}
