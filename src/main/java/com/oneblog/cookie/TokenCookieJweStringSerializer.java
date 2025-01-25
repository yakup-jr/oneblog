package com.oneblog.cookie;

import com.nimbusds.jose.*;
import com.nimbusds.jwt.EncryptedJWT;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.Setter;

import java.util.Date;
import java.util.function.Function;

public class TokenCookieJweStringSerializer implements Function<Token, String> {

	private final JWEEncrypter jweEncrypter;

	@Setter
	private JWEAlgorithm jweAlgorithm = JWEAlgorithm.DIR;

	@Setter
	private EncryptionMethod encryptionMethod = EncryptionMethod.A128GCM;

	public TokenCookieJweStringSerializer(JWEEncrypter jweEncrypter) {
		this.jweEncrypter = jweEncrypter;
	}

	public TokenCookieJweStringSerializer(
		JWEEncrypter jweEncrypter, JWEAlgorithm jweAlgorithm, EncryptionMethod encryptionMethod) {
		this.jweEncrypter = jweEncrypter;
		this.jweAlgorithm = jweAlgorithm;
		this.encryptionMethod = encryptionMethod;
	}

	@Override
	public String apply(Token token) {
		JWEHeader jwsHeader =
			new JWEHeader.Builder(jweAlgorithm, encryptionMethod).keyID(token.id().toString()).build();
		JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().jwtID(token.id().toString()).subject(token.subject())
		                                                   .issueTime(Date.from(token.createdAt()))
		                                                   .expirationTime(Date.from(token.expiresAt()))
		                                                   .claim("authorities", token.authorities()).build();
		EncryptedJWT encryptedJWT = new EncryptedJWT(jwsHeader, claimsSet);
		try {
			encryptedJWT.encrypt(jweEncrypter);
			return encryptedJWT.serialize();
		} catch (JOSEException e) {
			throw new RuntimeException(e);
		}
	}

}
