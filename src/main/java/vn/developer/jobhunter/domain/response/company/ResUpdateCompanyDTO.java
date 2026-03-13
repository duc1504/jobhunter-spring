package vn.developer.jobhunter.domain.response.company;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResUpdateCompanyDTO {

    private long id;
    private String name;
    private String description;
    private String address;
    private String logo;
    private String updatedBy;
}
