package com.re.badminton_court.service.cluster_and_court;

import com.re.badminton_court.exception.ResourceNotFoundException;
import com.re.badminton_court.model.dto.badminton_cluster.BadmintonClusterRequest;
import com.re.badminton_court.model.dto.badminton_cluster.BadmintonClusterResponse;
import com.re.badminton_court.model.entity.BadmintonCluster;
import com.re.badminton_court.model.entity.User;
import com.re.badminton_court.model.enums.Role;
import com.re.badminton_court.repository.BadmintonClusterRepository;
import com.re.badminton_court.repository.UserRepository;
import com.re.badminton_court.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class BadmintonClusterImpl implements BadmintonClusterService {
    private final BadmintonClusterRepository clusterRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<BadmintonClusterResponse> findAll(Pageable pageable) {
        return clusterRepository.findAll(pageable).map(BadmintonClusterImpl::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BadmintonClusterResponse findById(Long id) {
        return toResponse(findCluster(id));
    }

    @Override
    public BadmintonClusterResponse create(BadmintonClusterRequest request) {
        if (clusterRepository.existsByNameAndAddress(request.getName(), request.getAddress())) {
            throw new IllegalArgumentException("Cluster already exists with same name and address");
        }

        BadmintonCluster cluster = BadmintonCluster.builder()
                .name(request.getName())
                .address(request.getAddress())
                .hotLine(request.getHotLine())
                .manager(findManagerOrNull(request.getManagerId()))
                .build();

        return toResponse(clusterRepository.save(cluster));
    }

    @Override
    public BadmintonClusterResponse update(Long id, BadmintonClusterRequest request) {
        BadmintonCluster cluster = findCluster(id);
        cluster.setName(request.getName());
        cluster.setAddress(request.getAddress());
        cluster.setHotLine(request.getHotLine());
        if (request.getManagerId() != null) {
            cluster.setManager(findManagerOrNull(request.getManagerId()));
        }
        return toResponse(clusterRepository.save(cluster));
    }

    @Override
    public BadmintonClusterResponse assignManager(Long id, Long managerId) {
        BadmintonCluster cluster = findCluster(id);
        cluster.setManager(findManagerOrNull(managerId));
        return toResponse(clusterRepository.save(cluster));
    }

    @Override
    public void delete(Long id) {
        if (!clusterRepository.existsById(id)) {
            throw new ResourceNotFoundException("BadmintonCluster", "id", id);
        }
        clusterRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BadmintonClusterResponse> findMyClusters(Pageable pageable) {
        return clusterRepository.findByManagerId(currentUser().getId(), pageable)
                .map(BadmintonClusterImpl::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public BadmintonClusterResponse findMyClusterById(Long id) {
        return toResponse(findMyCluster(id));
    }

    @Override
    public BadmintonClusterResponse updateMyCluster(Long id, BadmintonClusterRequest request) {
        BadmintonCluster cluster = findMyCluster(id);
        cluster.setName(request.getName());
        cluster.setAddress(request.getAddress());
        cluster.setHotLine(request.getHotLine());
        return toResponse(clusterRepository.save(cluster));
    }

    private BadmintonCluster findCluster(Long id) {
        return clusterRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("BadmintonCluster", "id", id));
    }

    private BadmintonCluster findMyCluster(Long id) {
        BadmintonCluster cluster = findCluster(id);
        User user = currentUser();
        if (cluster.getManager() == null || !cluster.getManager().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You do not manage this cluster");
        }
        return cluster;
    }

    private User findManagerOrNull(Long managerId) {
        if (managerId == null) {
            return null;
        }
        User manager = userRepository.findById(managerId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", managerId));
        if (manager.getRole() != Role.MANAGER) {
            throw new IllegalArgumentException("User is not a manager");
        }
        return manager;
    }

    private User currentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof CustomUserDetails customUserDetails) {
            return customUserDetails.getUser();
        }
        throw new IllegalArgumentException("Authenticated user not found");
    }

    public static BadmintonClusterResponse toResponse(BadmintonCluster cluster) {
        User manager = cluster.getManager();
        return new BadmintonClusterResponse(
                cluster.getId(),
                cluster.getName(),
                cluster.getAddress(),
                cluster.getHotLine(),
                manager != null ? manager.getId() : null,
                manager != null ? manager.getFullName() : null
        );
    }
}
