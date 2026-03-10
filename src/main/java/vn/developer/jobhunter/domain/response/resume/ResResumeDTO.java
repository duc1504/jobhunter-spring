package vn.developer.jobhunter.domain.response.resume;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.developer.jobhunter.util.constant.ResumeStateEnum;

@Getter
@Setter
public class ResResumeDTO {
    private long id;
    private String email;
    private String uri;
    private ResumeStateEnum state;
    private Instant createdAt;
    private String createdBy;
    private User user;
    private Job job;
    

    @Getter
    @Setter
    public static class User {
        private long id;
        private String name;
    }
    @Getter
    @Setter
    public static class Job {
        private long id;
        private String name;
    }

}
