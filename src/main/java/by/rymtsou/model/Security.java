package by.rymtsou.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Component
@Data
public class Security {
    private Long id;
    @NotBlank(message = "Login cannot be empty.")
    @Size(min = 4, max = 40, message = "Login must be between 4 and 40 characters.")
    private String login;

    @NotBlank(message = "Password cannot be empty")
    @Size(min = 6, message = "Password must be at least 6 characters long.")
    private String password;

    @PastOrPresent(message = "Created timestamp cannot be in the future.")
    private Timestamp created;

    @PastOrPresent(message = "Updated timestamp cannot be in the future.")
    private Timestamp updated;

    @NotNull(message = "User ID cannot be null.")
    private Long userId;
}
