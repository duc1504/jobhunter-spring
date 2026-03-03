package vn.developer.jobhunter.domain.dto;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestUpdateUserDTO {
    private long id;
    private String name;
    private String gender;
    private String address;
    private int age;
    private Instant updatedAt;
}
