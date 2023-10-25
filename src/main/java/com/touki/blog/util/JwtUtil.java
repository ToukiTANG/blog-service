package com.touki.blog.util;


import com.touki.blog.constant.DelimiterConstant;
import com.touki.blog.model.dto.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Touki
 */
@Configuration
public class JwtUtil {

    public static String secretKey;

    @Value("${touki.secret-key}")
    public void setSecret(String sKey) {
        secretKey = sKey;
    }

    public static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS256;
    /**
     * ac_token2天过期
     */
    public static final long AC_EXPIRATION = 2 * 86400 * 1000L;

    private static String getJti() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String createAccessToken(AuthUser authUser) {
        String collect = extractAuthorities(authUser);
        return Jwts.builder().setSubject(String.valueOf(authUser.getUsername()))
                .claim("authorities", collect)
                .setId(getJti())
                .signWith(generalKey(), ALGORITHM)
                .setExpiration(new Date(System.currentTimeMillis() + AC_EXPIRATION))
                .compact();
    }

    private static String extractAuthorities(AuthUser authUser) {
        Collection<? extends GrantedAuthority> authorities = authUser.getAuthorities();
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(DelimiterConstant.COMMA));
    }

    public static Claims parseJwt(String jwtToken) {
        SecretKey secretKey = generalKey();
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(jwtToken)
                .getBody();
    }

    public static SecretKey generalKey() {
        byte[] encodeKey = Base64.getDecoder().decode(secretKey);
        return new SecretKeySpec(encodeKey, 0, encodeKey.length, "HmacSHA256");
    }
}
