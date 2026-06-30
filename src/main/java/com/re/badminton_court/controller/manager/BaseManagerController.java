package com.re.badminton_court.controller.manager;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/manager")
@PreAuthorize("hasRole('MANAGER')")
public class BaseManagerController {
}
