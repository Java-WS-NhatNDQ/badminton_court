package com.re.badminton_court.controller.open;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class CourtController {

    @GetMapping("/courts")
    public ResponseEntity<?> getAll() {
        return null;
    }

    @GetMapping("/courts/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return null;
    }

    @GetMapping("/clusters/{clusterId}/courts")
    public ResponseEntity<?> getByClusterId(@PathVariable Long clusterId) {
        return null;
    }
}
