package vn.developer.jobhunter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import vn.developer.jobhunter.domain.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {    
}
