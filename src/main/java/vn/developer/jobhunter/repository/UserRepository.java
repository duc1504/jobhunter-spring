package vn.developer.jobhunter.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import vn.developer.jobhunter.domain.Company;
import vn.developer.jobhunter.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>,JpaSpecificationExecutor<User> {
       
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    User findByRefreshTokenAndEmail(String refreshToken, String email);
}
