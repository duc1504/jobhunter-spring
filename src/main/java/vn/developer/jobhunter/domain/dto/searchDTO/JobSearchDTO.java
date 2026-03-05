package vn.developer.jobhunter.domain.dto.searchDTO;

import lombok.Getter;
import lombok.Setter;
import vn.developer.jobhunter.util.constant.LevelEnum;

@Getter
@Setter
public class JobSearchDTO {

    private String name;

    private String location;

    private LevelEnum level;

    private Boolean active;

    private Long skillId;

    private Long companyId;

    private Double minSalary;

    private Double maxSalary;
}
