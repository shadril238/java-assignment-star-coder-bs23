package com.shadril.taskmanagementjava.utils;

import com.shadril.taskmanagementjava.constant.AppConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Date;
import java.util.List;
import java.util.Random;

@Slf4j
@Component
public class JwtUtils {
    private static final Random RANDOM = new SecureRandom();
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static Boolean hasTokenExpired(String token){
        Claims claims = Jwts.parser().setSigningKey(AppConstant.TOKEN_SECRET).parseClaimsJws(token).getBody();
        Date tokenExpirationDate = claims.getExpiration();
        Date today = new Date();
        return tokenExpirationDate.before(today);
    }

    public static String generateToken(String username, List<String> roles){
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles)
                .setExpiration(new Date(System.currentTimeMillis()+AppConstant.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, AppConstant.TOKEN_SECRET)
                .compact();
    }
    private static String generateRandomString(int length){
        StringBuilder returnValue = new StringBuilder(length);
        for (int i = 0; i < length; i++)
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        return new String(returnValue);
    }

    public static String extractUser(String token) {
        return Jwts.parser().setSigningKey(AppConstant.TOKEN_SECRET).parseClaimsJws(token).getBody().getSubject();
    }

    public static List<String> extractUserRoles(String token) {
        Claims claims = Jwts.parser().setSigningKey(AppConstant.TOKEN_SECRET).parseClaimsJws(token).getBody();
        return  (List<String>) claims.get("roles");
    }
}
