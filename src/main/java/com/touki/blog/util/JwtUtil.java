package com.touki.blog.util;


import com.touki.blog.model.vo.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

/**
 * @author Touki
 */
public abstract class JwtUtil {
    public static final String JWT_KEY = "dahfuduabgjadbgaugweyugbajdgbnadaiughweiuhgadjnf";
    public static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS256;
    public static final long AC_EXPIRATION = 86400L;
    public static final long RE_EXPIRATION = 604800L;

    public static String getJti() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String createAccessToken(AuthUser authUser) {
        Collection<? extends GrantedAuthority> authorities = authUser.getAuthorities();
        return Jwts.builder().setSubject(String.valueOf(authUser.getUserId()))
                .claim("authorities", authorities)
                .setId(getJti())
                .signWith(generalKey(), ALGORITHM)
                .setExpiration(new Date(System.currentTimeMillis() + AC_EXPIRATION))
                .compact();
    }

    public static String createRefreshToken(AuthUser authUser) {
        Collection<? extends GrantedAuthority> authorities = authUser.getAuthorities();
        return Jwts.builder().setSubject(String.valueOf(authUser.getUserId()))
                .claim("authorities", authorities)
                .setId(getJti())
                .signWith(generalKey(), ALGORITHM)
                .setExpiration(new Date(System.currentTimeMillis() + RE_EXPIRATION))
                .compact();
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
        byte[] encodeKey = Base64.getDecoder().decode(JwtUtil.JWT_KEY);
        return new SecretKeySpec(encodeKey, 0, encodeKey.length, "HmacSHA256");
    }
}
