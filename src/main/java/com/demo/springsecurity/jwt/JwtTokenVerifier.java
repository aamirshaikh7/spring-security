package com.demo.springsecurity.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtTokenVerifier extends OncePerRequestFilter {

    private final JwtConfig jwtConfig;
    private final SecretKey key;

    public JwtTokenVerifier(JwtConfig jwtConfig, SecretKey key) {
        this.jwtConfig = jwtConfig;
        this.key = key;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(jwtConfig.getAuthorizationHeader());

        if (Strings.isNullOrEmpty(header) || !header.startsWith(jwtConfig.getTokenPrefix())) {
            filterChain.doFilter(request, response);

            return;
        }

        String token = header.replace(jwtConfig.getTokenPrefix(), "");

        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);

            Claims body = claimsJws.getBody();

            String username = body.getSubject();

            List<Map<String, String>> authorities = (List<Map<String, String>>) body.get("authorities");

            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.get("authority")))
                    .collect(Collectors.toSet());

            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    simpleGrantedAuthorities
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (SignatureException exception) {
            throw new IllegalStateException("Invalid JWT signature");
        } catch (MalformedJwtException exception) {
            throw new IllegalStateException("Invalid JWT token " + token);
        } catch (ExpiredJwtException exception) {
            throw new IllegalStateException("Expired JWT token " + token);
        } catch (UnsupportedJwtException exception) {
            throw new IllegalStateException("Unsupported JWT token " + token);
        } catch (IllegalArgumentException exception) {
            throw new IllegalStateException("JWT claims string is empty.");
        }

//        passing filter to api

        filterChain.doFilter(request, response);
    }
}
