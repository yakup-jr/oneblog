package net.oneblog.auth.service;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoogleVerificationServiceTest {
    @InjectMocks
    private GoogleVerificationService googleVerificationService;

    @Mock
    private GoogleIdTokenVerifier verifier;
    @Mock
    private GoogleIdToken googleIdToken;
    @Mock
    private GoogleIdToken.Payload payload;

    @Test
    void verify_Success() throws GeneralSecurityException, IOException {
        String token = "\"Bearer\" \"token\" \"value\" \"valid-token\"";
        when(verifier.verify(anyString())).thenReturn(googleIdToken);
        when(googleIdToken.getPayload()).thenReturn(payload);
        when(payload.getEmailVerified()).thenReturn(true);

        GoogleIdToken.Payload result = googleVerificationService.verify(token);

        assertNotNull(result);
        assertEquals(payload, result);
    }

    @Test
    void verify_GoogleTokenNotExists() throws GeneralSecurityException, IOException {
        String token = "\"Bearer\" \"token\" \"value\" \"valid-token\"";
        when(verifier.verify(anyString())).thenReturn(null);

        assertThrows(GeneralSecurityException.class,
            () -> googleVerificationService.verify(token));
    }

    @Test
    void verify_EmailNotVerified() throws GeneralSecurityException, IOException {
        String token = "\"Bearer\" \"token\" \"value\" \"valid-token\"";
        when(verifier.verify(anyString())).thenReturn(googleIdToken);
        when(googleIdToken.getPayload()).thenReturn(payload);
        when(payload.getEmailVerified()).thenReturn(false);

        assertThrows(GeneralSecurityException.class,
            () -> googleVerificationService.verify(token));
    }

    @Test
    void verify_InvalidTokenFormat() {
        String token = "invalid-format";

        assertThrows(GeneralSecurityException.class,
            () -> googleVerificationService.verify(token));
    }
}