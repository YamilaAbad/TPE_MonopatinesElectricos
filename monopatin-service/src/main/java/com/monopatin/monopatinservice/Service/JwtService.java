package com.monopatin.monopatinservice.Service;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;
@Service
public class JwtService {

    private static final String SECRET_KEY="586E3272357538782F413F4428472B486250655368566B597033700676397924"; //clave secreta para firmar el token


    //obtengo la clave de firma desde la clave secreta en Base64
    private Key getKey(){
        byte[] keyByte= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyByte);
    }
    //obtengo el nombre de usuario desde el token JWT
    public String getUsernameFromToken(String token) {
        return getClaim(token, Claims::getSubject);
    }
    public boolean isTokenValid(String token) {
        final String username = getUsernameFromToken(token);
        return (!isTokenExpired(token) && username != null);
    }

    //obtengo todas las reclamaciones del token JWT
    private Claims getAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(getKey()).build().parseClaimsJws(token).getBody();
    }

    //obtengo una reclamación específica del token JWT
    public <T> T getClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    //obtengo la fecha de expiración del token JWT
    private Date getExpiration(String token) {
        return getClaim(token, Claims::getExpiration);
    }

    //verifica si el token JWT ha expirado
    private boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
}
