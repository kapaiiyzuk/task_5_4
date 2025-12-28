package org.example.task_5_1.controller;

import org.example.task_5_1.entity.Role;
import org.example.task_5_1.entity.User;
import org.example.task_5_1.sevice.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminUserController {

    private final UserService userService;

    public AdminUserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/admin/users")
    public String adminPage(Model model) {
        List<User> users = userService.getAll();
        List<Role> roles = userService.getAllRoles();

        model.addAttribute("users", users);
        model.addAttribute("roles", roles);

        model.addAttribute("newUser", new User());

        return "admin";
    }

    @PostMapping("/admin/users/save")
    public String saveUser(@ModelAttribute("user") User user,
                           @RequestParam(value = "roleNames", required = false) List<String> roleNames) {
        userService.save(user, roleNames);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin/users";
    }
}
