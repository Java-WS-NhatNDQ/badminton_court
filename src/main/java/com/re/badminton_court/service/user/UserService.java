package com.re.badminton_court.service.user;

import com.re.badminton_court.model.dto.user.UserCreateRequest;
import com.re.badminton_court.model.dto.user.UserResponse;
import com.re.badminton_court.model.dto.user.UserSearchRequest;
import com.re.badminton_court.model.dto.user.UserUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    Page<UserResponse> search(UserSearchRequest request, Pageable pageable);
    UserResponse findById(Long id);
    UserResponse create(UserCreateRequest request);
    UserResponse update(Long id, UserUpdateRequest request);
    void delete(Long id);
    UserResponse banUser(Long id);
    UserResponse unbanUser(Long id);
    void changePassword(String email, String oldPassword, String newPassword);
    void resetPassword(String email);
}
