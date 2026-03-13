package vn.developer.jobhunter.domain.request.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqUpdateUserDTO {

    private Long id;
    private String name;
    private String address;
    private Integer age;
    private String gender;
    private Long companyId;

}
