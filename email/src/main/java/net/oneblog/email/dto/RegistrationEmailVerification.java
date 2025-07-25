package net.oneblog.email.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Registration email verification.
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationEmailVerification {

	private String email;
	private String verificationCode;

}
