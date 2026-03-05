package vn.developer.jobhunter.domain.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    private Company company;

    @Getter
    @Setter
    public static class Company {
        private long id;
        private String name;
    }
}
