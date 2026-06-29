package com.re.badminton_court.model.dto.user;

import com.re.badminton_court.model.enums.Role;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UserUpdateRequest {
    private String password;
    private String fullName;

    @Email
    private String email;
    private String phoneNumber;
    private Role role;
    private Boolean enabled;
}
