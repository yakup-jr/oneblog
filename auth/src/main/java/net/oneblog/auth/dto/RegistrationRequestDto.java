package net.oneblog.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Registration request dto.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequestDto {

    private String name;
    private String username;
    private String email;
    private String password;

}
