package org.example.task_5_1.controller;

import org.example.task_5_1.entity.User;
import org.example.task_5_1.sevice.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserHomeController {

    private final UserService userService;

    public UserHomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/user")
    public String userHome(Model model, @AuthenticationPrincipal UserDetails principal) {
        User user = userService.findByUsername(principal.getUsername());
        model.addAttribute("user", user);
        return "user";
    }
}
