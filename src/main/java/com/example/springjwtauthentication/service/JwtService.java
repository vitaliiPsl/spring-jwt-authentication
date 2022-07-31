package com.example.springjwtauthentication.service;

import com.example.springjwtauthentication.model.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.token_life_time}")
    private long tokenLifeTime;

    public String buildToken(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        SecretKey secretKey = getSecretKey();

        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("authorities", authentication.getAuthorities())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(tokenLifeTime)))
                .signWith(secretKey)
                .compact();
    }

    public Jws<Claims> parse(String token) {
        SecretKey secretKey = getSecretKey();
        JwtParser parser = Jwts.parserBuilder().setSigningKey(secretKey).build();

        return parser.parseClaimsJws(token);
    }

    public Authentication tokenToAuthenticationMapper(String token) {
        Jws<Claims> claimsJws = parse(token);

        Claims body = claimsJws.getBody();
        String username = body.getSubject();

        List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");
        List<GrantedAuthority> grantedAuthorities = authorities.stream()
                .map(Map::values)
                .flatMap(Collection::stream)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(username, null, grantedAuthorities);
    }

    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }
}
