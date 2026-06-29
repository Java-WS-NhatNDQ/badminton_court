package com.re.badminton_court.model.dto.user;

import com.re.badminton_court.model.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UserCreateRequest {
    @NotBlank
    private String username;

    @NotBlank
    private String password;
    private String fullName;

    @NotBlank @Email
    private String email;
    private String phoneNumber;

    @NotNull
    private Role role;
    private Boolean enabled;
}
