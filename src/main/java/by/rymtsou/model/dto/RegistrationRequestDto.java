package by.rymtsou.model.dto;

import by.rymtsou.annotation.CustomAge;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegistrationRequestDto {
    @NotNull
    @Size(min = 2, max = 20)
    private String firstname;

    @NotNull
    @Size(min = 2, max = 20)
    private String secondName;

    @CustomAge
    private Integer age;

    @Email
    private String email;
    private String sex;

    @Pattern(regexp = "[0-9]{12}")
    private String telephoneNumber;

    @NotNull
    @NotBlank
    private String login;

    @NotNull
    @NotBlank
    private String password;
}
