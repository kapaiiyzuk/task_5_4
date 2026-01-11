package org.example.task_5_1.controller;

import org.example.task_5_1.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserRestController {

    @GetMapping("/api/me")
    public User me(@AuthenticationPrincipal User user) {
        return user;
    }
}
