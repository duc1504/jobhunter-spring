package vn.developer.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.developer.jobhunter.domain.dto.LoginDTO;
import vn.developer.jobhunter.domain.dto.ResLoginDTO;
import vn.developer.jobhunter.util.error.SecurityUtil;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class AuthController {
   private final AuthenticationManagerBuilder authenticationManagerBuidler;
   private final SecurityUtil securityUtil;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuidler, SecurityUtil securityUtil) {
        this.authenticationManagerBuidler = authenticationManagerBuidler;
        this.securityUtil = securityUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<ResLoginDTO> Login(@Valid @RequestBody LoginDTO loginDTO) {
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(),
                loginDTO.getPassword());

        Authentication authentication = authenticationManagerBuidler.getObject().authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = this.securityUtil.createAccessToken(authentication);
        ResLoginDTO resLoginDTO = new ResLoginDTO();
        resLoginDTO.setAccessToken(accessToken);
        return ResponseEntity.ok().body(resLoginDTO);
    }
}
