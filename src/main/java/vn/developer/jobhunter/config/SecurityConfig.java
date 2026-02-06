package vn.developer.jobhunter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
            // ❌ Tắt CSRF
            .csrf(csrf -> csrf.disable())

            // ❌ Tắt form login mặc định
            .formLogin(form -> form.disable())

            // ❌ Tắt http basic
            .httpBasic(basic -> basic.disable())

            // ✅ Cho phép tất cả request
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll()
            );

        return http.build();
    }
}
