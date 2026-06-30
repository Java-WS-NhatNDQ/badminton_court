package com.re.badminton_court.controller.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ManagerCourtController extends BaseManagerController {

    @GetMapping("/clusters/{clusterId}/courts")
    public ResponseEntity<?> getMyCourtsByCluster(@PathVariable Long clusterId) {
        return null;
    }

    @PostMapping("/clusters/{clusterId}/courts")
    public ResponseEntity<?> create(@PathVariable Long clusterId) {
        return null;
    }

    @GetMapping("courts/{courtId}")
    public ResponseEntity<?> getById(@PathVariable Long courtId) {
        return null;
    }

    @PutMapping("/courts/{courtId}")
    public ResponseEntity<?> update(@PathVariable Long courtId) {
        return null;
    }

    @PatchMapping("/courts/{courtId}/availability")
    public ResponseEntity<?> updateAvailability(@PathVariable Long courtId) {
        return null;
    }

    @DeleteMapping("/courts/{courtId}")
    public ResponseEntity<?> delete(@PathVariable Long courtId) {
        return null;
    }
}
