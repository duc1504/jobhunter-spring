package vn.developer.jobhunter.util;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

import vn.developer.jobhunter.domain.response.ResLoginDTO;

@Service
public class SecurityUtil {
    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;
    public SecurityUtil(JwtEncoder jwtEncoder ,JwtDecoder jwtDecoder) {
        this.jwtEncoder = jwtEncoder;
        this.jwtDecoder = jwtDecoder;
    }

    public static final MacAlgorithm JWT_ALGORITHM = MacAlgorithm.HS512;

    @Value("${developer.jwt.access-token-validity-in-seconds}")
    private long accessTokenExpiration;

    @Value("${developer.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    // create access token
    public String createAccessToken(String email, ResLoginDTO.UserLogin userLogin) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.accessTokenExpiration, ChronoUnit.SECONDS);
        List<String> listAuthority = new ArrayList<>();
        listAuthority.add("ROLE_USER_CREATE");
        listAuthority.add("ROLE_USER_UPDATE");
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", userLogin)
                .claim("permission", listAuthority)
                .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    // create refesh token
    public String createRefreshToken(String email, ResLoginDTO.UserLogin userLogin) {
        Instant now = Instant.now();
        Instant validity = now.plus(this.refreshTokenExpiration, ChronoUnit.SECONDS);
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(email)
                .claim("user", userLogin)
                .build();
        JwsHeader jwsHeader = JwsHeader.with(JWT_ALGORITHM).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, claims)).getTokenValue();
    }

    private static String extractPrincipal(Authentication authentication) {
        if (authentication == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        System.out.println("Principal class: " + principal.getClass());

        if (principal instanceof UserDetails userDetails) {
            return userDetails.getUsername();
        }

        if (principal instanceof String) {
            return (String) principal;
        }
        if (principal instanceof Jwt jwt) {
            return jwt.getSubject();
        }

        return null;
    }

    public static Optional<String> getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        return Optional.ofNullable(
                extractPrincipal(securityContext.getAuthentication()));

    }

    public Optional<String> checkValidRefreshToken(String refreshToken) {
    try {
        Jwt jwt = jwtDecoder.decode(refreshToken);
        Instant expiresAt = jwt.getExpiresAt();
        if (expiresAt != null && expiresAt.isBefore(Instant.now())) {
            return Optional.empty();
        }
        String email = jwt.getSubject();
        return Optional.ofNullable(email);
    } catch (Exception e) {
        return Optional.empty();
    }
}

}
