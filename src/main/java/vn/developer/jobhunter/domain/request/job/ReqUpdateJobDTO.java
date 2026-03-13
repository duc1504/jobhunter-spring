package vn.developer.jobhunter.domain.request.job;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import vn.developer.jobhunter.util.constant.LevelEnum;

@Getter
@Setter
public class ReqUpdateJobDTO {

    private Long id;
    private String name;
    private String location;
    private Double salary;
    private Integer quantity;
    private LevelEnum level;
    private String description;
    private Instant startDate;
    private Instant endDate;
    private Boolean active;
    // relation
    private Long companyId;
    private List<Long> skillIds;

}