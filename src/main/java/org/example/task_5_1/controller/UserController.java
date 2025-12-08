package org.example.task_5_1.controller;

import org.example.task_5_1.entity.User;
import org.example.task_5_1.sevice.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", service.getAll());
        return "users";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        return "user-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        User user = service.getById(id);
        model.addAttribute("user", user);
        return "user-form";
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") User user) {
        service.save(user);
        return "redirect:/users";
    }

    @DeleteMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        service.delete(id);
        return "redirect:/users";
    }
}
