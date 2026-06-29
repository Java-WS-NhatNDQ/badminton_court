package com.re.badminton_court.model.dto.user;

import com.re.badminton_court.model.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UserSearchRequest {
    private String keyword;
    private Role role;
    private Boolean enabled;
}
