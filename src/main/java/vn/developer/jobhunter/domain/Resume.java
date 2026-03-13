package vn.developer.jobhunter.domain;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import vn.developer.jobhunter.util.SecurityUtil;
import vn.developer.jobhunter.util.constant.ResumeStateEnum;

@Entity
@Table(name = "resumes")
@Getter
@Setter
public class Resume {
     @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "email không được để trống")
    private String email;
    @NotBlank(message = "uri không được để trống(upload CV chưa thành công)")
    private String uri;
    @Enumerated(EnumType.STRING)
    private ResumeStateEnum state;
    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

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
