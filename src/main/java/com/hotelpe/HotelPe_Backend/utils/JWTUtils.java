package com.hotelpe.HotelPe_Backend.utils;

import com.hotelpe.HotelPe_Backend.client.AutenticacionClient;
import com.hotelpe.HotelPe_Backend.dto.GetPublicKeyDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Objects;
import java.util.function.Function;

@Service
@Slf4j
public class JWTUtils {

    private PublicKey publicKey;

    @Autowired
    AutenticacionClient autenticacionClient;

    @PostConstruct
    private void initializeKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        ResponseEntity<GetPublicKeyDto> publicKey =  autenticacionClient.getPublicKey();
        log.info("publicKey dddd " + publicKey.getBody().getToken());
        byte[] keyBytes = Base64.getDecoder().decode(Objects.requireNonNull(publicKey.getBody().getToken()));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        this.publicKey =  keyFactory.generatePublic(spec);
    }

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    private <T> T extractClaims(String token, Function<Claims, T> claimsTFunction) {
        return claimsTFunction.apply(Jwts.parser().verifyWith(this.publicKey).build().parseSignedClaims(token).getPayload());
    }

    public boolean isValidToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token, Claims::getExpiration).before(new Date());
    }
}
