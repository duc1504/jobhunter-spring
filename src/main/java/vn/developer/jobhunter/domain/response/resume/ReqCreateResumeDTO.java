package vn.developer.jobhunter.domain.response.resume;
import lombok.Getter;
import lombok.Setter;
import vn.developer.jobhunter.util.constant.ResumeStateEnum;

@Getter
@Setter
public class ReqCreateResumeDTO {
    private long id;
    private String email;
    private String uri;
    private ResumeStateEnum state;
    private long userId;
    private long jobId;

}
