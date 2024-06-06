package com.ApiGateway.jwt.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;

@Component
public class JwtUtil {

    private static final Logger logger= LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String SECRET;

    public void validateToken(final String token) {
        System.out.println("JWT Secret: " + SECRET); // Debug statement
        //System.out.println("Token: " + token);
        logger.info("The token generated is : {}",token);
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}