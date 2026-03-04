package vn.developer.jobhunter.domain;
import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.developer.jobhunter.util.constant.GenderEnum;
import vn.developer.jobhunter.util.error.SecurityUtil;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User {
     @Id
     @GeneratedValue(strategy = GenerationType.IDENTITY)
     private long id;
    private String name;
    @NotBlank(message = "email không được để trống")
    private String email;
    @NotBlank(message = "mật khẩu không được để trống")
    private String password;
    private int age;
    @Enumerated(EnumType.STRING)
    private GenderEnum gender;
    private String address;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String refreshToken;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;
    @PrePersist
     public void prePersist() {
          createdAt = Instant.now();
          createdBy = SecurityUtil.getCurrentUserLogin().orElse("system");
     }
     @PreUpdate
     public void preUpdate() {
          updatedAt = Instant.now();
          updatedBy = SecurityUtil.getCurrentUserLogin().orElse("system");
     }
}

