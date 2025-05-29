package lk.buses.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lk.buses.common.core.enums.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.access-token-expiration:3600}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration:86400}")
    private long refreshTokenExpiration;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(UUID userId, String mobileNumber, UserRole role) {
        return generateToken(userId, mobileNumber, role, accessTokenExpiration);
    }

    public String generateRefreshToken(UUID userId, String mobileNumber, UserRole role) {
        return generateToken(userId, mobileNumber, role, refreshTokenExpiration);
    }

    private String generateToken(UUID userId, String mobileNumber, UserRole role, long expiration) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration * 1000);

        return Jwts.builder()
                .subject(userId.toString())
                .claim("mobile", mobileNumber)
                .claim("role", role.name())
                .issuedAt(now)
                .expiration(expiryDate)
                .signWith(getSigningKey())
                .compact();
    }

    public Claims validateToken(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            log.error("Invalid JWT token: {}", e.getMessage());
            throw new InvalidTokenException("Invalid token");
        }
    }

    public UUID getUserIdFromToken(String token) {
        Claims claims = validateToken(token);
        return UUID.fromString(claims.getSubject());
    }

    public UserRole getRoleFromToken(String token) {
        Claims claims = validateToken(token);
        return UserRole.valueOf(claims.get("role", String.class));
    }
}
