package com.touki.blog.util;


import com.touki.blog.constant.DelimiterConstant;
import com.touki.blog.constant.TokenType;
import com.touki.blog.model.dto.AuthUser;
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
import java.util.stream.Collectors;

/**
 * @author Touki
 */
public abstract class JwtUtil {
    public static final String JWT_KEY = "dahfuduabgjadbgaugweyugbajdgbnadaiughweiuhgadjnf";
    public static final SignatureAlgorithm ALGORITHM = SignatureAlgorithm.HS256;
    /**
     * ac_token一天过期
     */
    public static final long AC_EXPIRATION = 86400 * 1000L;
    /**
     * re_refresh七天过期
     */
    public static final long RE_EXPIRATION = 604800 * 1000L;

    public static String getJti() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String createAccessToken(AuthUser authUser) {
        String collect = extractAuthorities(authUser);
        return Jwts.builder().setSubject(String.valueOf(authUser.getUsername()))
                .claim("authorities", collect)
                .claim("type", TokenType.ACCESS_TOKEN)
                .setId(getJti())
                .signWith(generalKey(), ALGORITHM)
                .setExpiration(new Date(System.currentTimeMillis() + AC_EXPIRATION))
                .compact();
    }

    private static String extractAuthorities(AuthUser authUser) {
        Collection<? extends GrantedAuthority> authorities = authUser.getAuthorities();
        return authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(DelimiterConstant.COMMA));
    }

    public static String createRefreshToken(AuthUser authUser) {
        String collect = extractAuthorities(authUser);
        return Jwts.builder().setSubject(String.valueOf(authUser.getUsername()))
                .claim("authorities", collect)
                .claim("type", TokenType.REFRESH_TOKEN)
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
