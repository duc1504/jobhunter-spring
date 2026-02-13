package vn.developer.jobhunter.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.developer.jobhunter.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {    
    Optional<User> findByEmail(String email);
}
