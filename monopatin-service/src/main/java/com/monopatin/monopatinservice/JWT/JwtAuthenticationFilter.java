package com.monopatin.monopatinservice.JWT;

import com.monopatin.monopatinservice.Service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = getTokenFromRequest(request);

        if (StringUtils.hasText(token) && jwtService.isTokenValid(token)) {
            // Si el token es válido, autentica al usuario
            String username = jwtService.getUsernameFromToken(token);

            // Crear UserDetails directamente desde la solicitud
            UserDetails userDetails = createUserDetailsFromRequest(username, request);

            if (jwtService.isTokenValid(token, userDetails)) {
                // Crea una instancia de UsernamePasswordAuthenticationToken y establece la autenticación en el contexto de seguridad
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            // Extrae y devuelve el token del encabezado 'Bearer'
            return authHeader.substring(7);
        }

        return null;
    }

    private UserDetails createUserDetailsFromRequest(String username, HttpServletRequest request) {
        return User.builder()
                .username(username)
                .password("")
                .roles("ADMIN")
                .build();
    }
}
