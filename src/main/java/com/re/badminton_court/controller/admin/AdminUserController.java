package com.re.badminton_court.controller.admin;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.re.badminton_court.model.dto.ApiResponse;
import com.re.badminton_court.model.dto.user.UserCreateRequest;
import com.re.badminton_court.model.dto.user.UserResponse;
import com.re.badminton_court.model.dto.user.UserSearchRequest;
import com.re.badminton_court.model.dto.user.UserUpdateRequest;
import com.re.badminton_court.service.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AdminUserController extends BaseAdminController {
    private final UserService userService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> search(
            @ModelAttribute UserSearchRequest request,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserResponse> result = userService.search(request, pageable);
        return ResponseEntity.ok(ApiResponse.success(result, "Get users successfully!!!"));
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable Long id) {
        UserResponse result = userService.findById(id);
        return ResponseEntity.ok(ApiResponse.success(result, "Get user successfully!"));
    }

    @PostMapping("/users")
    public ResponseEntity<ApiResponse<UserResponse>> create(@Valid @RequestBody UserCreateRequest request) {
        UserResponse result = userService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(result, "Create user successfully!!"));
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {
        UserResponse result = userService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(result, "Update user successfully!"));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Delete user successfully!!"));
    }

    @PostMapping("/users/{id}/ban")
    public ResponseEntity<ApiResponse<UserResponse>> banUser(@PathVariable Long id) {
        UserResponse result = userService.banUser(id);
        return ResponseEntity.ok(ApiResponse.success(result, "Ban user successfully!!"));
    }

    @PostMapping("/users/{id}/unban")
    public ResponseEntity<ApiResponse<UserResponse>> unbanUser(@PathVariable Long id) {
        UserResponse result = userService.unbanUser(id);
        return ResponseEntity.ok(ApiResponse.success(result, "Unban user successfully!"));
    }
}
