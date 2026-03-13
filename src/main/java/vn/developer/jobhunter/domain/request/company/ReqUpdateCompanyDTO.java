package vn.developer.jobhunter.domain.request.company;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReqUpdateCompanyDTO {

    private Long id;
    private String name;
    private String description;
    private String address;
    private String logo;

}
