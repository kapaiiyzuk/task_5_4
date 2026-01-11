package org.example.task_5_1.controller;

import org.example.task_5_1.sevice.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminPageController {
    private final UserService userService;

    public AdminPageController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"/admin", "/admin/users"})
    public String adminPage(Model model) {
        model.addAttribute("users", userService.getAll());
        return "admin";
    }
}

