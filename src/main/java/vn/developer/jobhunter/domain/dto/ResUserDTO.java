package vn.developer.jobhunter.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResUserDTO {

    private Long id;
    private String name;
    private String email;

}
