package cool.cfapps.springboottesting.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class EmployeeDto {
    private long id;
    private String firstName;
    private String lastName;
    private String email;

    public EmployeeDto copyOf() {
        return EmployeeDto.builder()
                .id(this.id)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .email(this.email)
                .build();
    }
}
