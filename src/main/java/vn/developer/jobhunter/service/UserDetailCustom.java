package vn.developer.jobhunter.service;

import java.util.Collections;

import org.hibernate.mapping.List;
import org.springframework.boot.autoconfigure.security.SecurityProperties.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import vn.developer.jobhunter.repository.UserRepository;

// @Component("userDetailsService")
@Service
public class UserDetailCustom implements UserDetailsService {
    private final UserRepository userRepository;

    public UserDetailCustom(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
  @Override
public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        vn.developer.jobhunter.domain.User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(),
            Collections.singletonList(
                    new SimpleGrantedAuthority("ROLE_USER")
            )
    );
}

    
}
