package cool.cfapps.springboottesting.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class CreateEmployeeDto {
    private String firstName;
    private String lastName;
    private String email;

    public CreateEmployeeDto copyOf() {
        return CreateEmployeeDto.builder()
                .firstName(this.firstName)
                .lastName(this.lastName)
                .email(this.email)
                .build();
    }
}