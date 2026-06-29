package com.re.badminton_court.service.user;

import com.re.badminton_court.exception.ResourceNotFoundException;
import com.re.badminton_court.model.dto.user.UserCreateRequest;
import com.re.badminton_court.model.dto.user.UserResponse;
import com.re.badminton_court.model.dto.user.UserSearchRequest;
import com.re.badminton_court.model.dto.user.UserUpdateRequest;
import com.re.badminton_court.model.entity.User;
import com.re.badminton_court.repository.UserRepository;
import com.re.badminton_court.repository.UserSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Page<UserResponse> search(UserSearchRequest request, Pageable pageable) {
        var spec = UserSpecification.withCriteria(request);
        Page<User> userPage = userRepository.findAll(spec, pageable);
        List<UserResponse> dtos = userPage.getContent().stream()
                .map(UserServiceImpl::toUserResponse)
                .collect(Collectors.toList());
        return new PageImpl<>(dtos, pageable, userPage.getTotalElements());
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return toUserResponse(user);
    }

    @Override
    public UserResponse create(UserCreateRequest request) {
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .fullName(request.getFullName())
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .role(request.getRole())
                .enabled(request.getEnabled() != null ? request.getEnabled() : true)
                .build();
        User saved = userRepository.save(user);
        return toUserResponse(saved);
    }

    @Override
    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getPhoneNumber() != null) {
            user.setPhoneNumber(request.getPhoneNumber());
        }
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        if (request.getEnabled() != null) {
            user.setEnabled(request.getEnabled());
        }
        User saved = userRepository.save(user);
        return toUserResponse(saved);
    }

    @Override
    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("User", "id", id);
        }
        userRepository.deleteById(id);
    }

    private static UserResponse toUserResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .build();
    }
}
