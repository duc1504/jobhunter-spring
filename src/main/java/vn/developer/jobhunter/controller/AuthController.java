package vn.developer.jobhunter.controller;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.developer.jobhunter.domain.User;
import vn.developer.jobhunter.domain.request.ReqLoginDTO;
import vn.developer.jobhunter.domain.response.ResLoginDTO;
import vn.developer.jobhunter.service.UserService;
import vn.developer.jobhunter.util.SecurityUtil;
import vn.developer.jobhunter.util.annotation.ApiMessage;
import vn.developer.jobhunter.util.error.IdInvaliException;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthenticationManagerBuilder authenticationManagerBuidler;
    private final SecurityUtil securityUtil;
    private final UserService userService;
    @Value("${developer.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuidler, SecurityUtil securityUtil,
            UserService userService) {
        this.authenticationManagerBuidler = authenticationManagerBuidler;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResLoginDTO> Login(@Valid @RequestBody ReqLoginDTO reqLoginDTO) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                reqLoginDTO.getUsername(),
                reqLoginDTO.getPassword());
        // Xác thực người dùng => cần viết hàm loadUserByUsername
        Authentication authentication = authenticationManagerBuidler.getObject().authenticate(authToken);
        // Set thông tin người dùng với SecurityContextHolder (sau này sẽ dùng)
        SecurityContextHolder.getContext().setAuthentication(authentication);
        ResLoginDTO resLoginDTO = new ResLoginDTO();
        User user = this.userService.handleGetUserByEmail(reqLoginDTO.getUsername());
        if (user != null) {
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(user.getId(), user.getEmail(), user.getName());
            resLoginDTO.setUser(userLogin);

        }

        String accessToken = this.securityUtil.createAccessToken(authentication.getName(), resLoginDTO.getUser());
        resLoginDTO.setAccessToken(accessToken);
        String refreshToken = this.securityUtil.createRefreshToken(reqLoginDTO.getUsername(), resLoginDTO.getUser());
        this.userService.updateUserToken(reqLoginDTO.getUsername(), refreshToken);
        ResponseCookie cookie = ResponseCookie.from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .sameSite("None")
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(resLoginDTO);
    }

    @GetMapping("/auth/account")
    @ApiMessage(value = "fetch account")
    public ResponseEntity<ResLoginDTO.ResAccountDTO> getAccount() {
        String email = this.securityUtil.getCurrentUserLogin().orElse(null);
        User user = this.userService.handleGetUserByEmail(email);
        ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin();
        if (user != null) {
            userLogin.setId(user.getId());
            userLogin.setEmail(user.getEmail());
            userLogin.setName(user.getName());
        }
        return ResponseEntity.ok().body( new ResLoginDTO.ResAccountDTO(userLogin));
    }

    // refresh token
    @PostMapping("/auth/refresh-token")
    @ApiMessage(value = "Get user refresh token")
    public ResponseEntity<ResLoginDTO> refreshToken(@CookieValue(value = "refresh_token") String refreshToken) {
        String email = securityUtil.checkValidRefreshToken(refreshToken).orElse("");
        User user = this.userService.handleGetUserByRefreshToken(refreshToken, email);
        if (user == null) {
            throw new IdInvaliException("User not found with refreshToken=" + refreshToken);
        }
       ResLoginDTO resLoginDTO = new ResLoginDTO();
            ResLoginDTO.UserLogin userLogin = new ResLoginDTO.UserLogin(user.getId(), user.getEmail(), user.getName());
            resLoginDTO.setUser(userLogin);

        String accessToken = this.securityUtil.createAccessToken(email, resLoginDTO.getUser());
        resLoginDTO.setAccessToken(accessToken);
        String newRefreshToken = this.securityUtil.createRefreshToken(email, resLoginDTO.getUser());
        this.userService.updateUserToken(email, newRefreshToken);
        ResponseCookie cookie = ResponseCookie.from("refresh_token", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .sameSite("None")
                .build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(resLoginDTO);
    }

    // logout
    @PostMapping("/auth/logout")
    public ResponseEntity logout() {
         String email = this.securityUtil.getCurrentUserLogin().orElse(null);
         if (email.equals("")) {
            throw new IdInvaliException("Access token not found");
         }
         this.userService.updateUserToken(email, null);
         ResponseCookie deleteCookie = ResponseCookie.from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("None")
                .build();
                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, deleteCookie.toString()).build();
    }

}
