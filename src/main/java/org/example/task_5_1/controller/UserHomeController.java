package org.example.task_5_1.controller;

import org.example.task_5_1.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserHomeController {
    @GetMapping("/user")
    public String userHome(Model model,
                           @AuthenticationPrincipal User user) {
        model.addAttribute("user", user);
        return "user-home";
    }
}
