package com.re.badminton_court.controller.manager;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 cum. sa^n do admin quan? li' CRUD va` gan' cho manager, manager chi? co' quye^`n xem
 va` update thong tin cluster ma` manager do quan? li'
* */

@RestController
@RequiredArgsConstructor
public class ManagerClusterController extends BaseManagerController {

    @GetMapping("/clusters")
    public ResponseEntity<?> getAllMyClusters() {
        return null;
    }

    @GetMapping("/clusters/{id}")
    public ResponseEntity<?> getMyClusterDetails(@PathVariable Long id) {
        return null;
    }

    @PutMapping("/clusters/{id}")
    public ResponseEntity<?> update(@PathVariable Long id) {
        return null;
    }
}
