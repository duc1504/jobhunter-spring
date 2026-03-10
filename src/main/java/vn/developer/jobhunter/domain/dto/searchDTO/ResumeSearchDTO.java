package vn.developer.jobhunter.domain.dto.searchDTO;

import lombok.Getter;
import lombok.Setter;
import vn.developer.jobhunter.util.constant.ResumeStateEnum;

@Getter
@Setter
public class ResumeSearchDTO {
    private String email;
    private ResumeStateEnum state;
}
