package com.re.badminton_court.controller.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*
 admin quan? li' hoan` toan` cum. san^ va` gan' cum. cho manager
* */

@RestController
@RequiredArgsConstructor
public class AdminClusterController extends BaseAdminController {

    @GetMapping("/clusters")
    public ResponseEntity<?> getAll() {
        return null;
    }

    @GetMapping("/clusters/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        return null;
    }

    @PostMapping("/clusters")
    public ResponseEntity<?> create() {
        return null;
    }

    @PutMapping("/clusters/{id}")
    public ResponseEntity<?> update(@PathVariable Long id) {
        return null;
    }

    @DeleteMapping("/clusters/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        return null;
    }
}
