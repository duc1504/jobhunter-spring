package vn.developer.jobhunter.domain.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResCreateUserDTO {
    private long id;
    private String name;
    private String email;
    private String gender;
    private String address;
     private int age;
     private Instant createdAt;
}
