package org.example.task_5_1.controller;

import lombok.RequiredArgsConstructor;
import org.example.task_5_1.entity.Role;
import org.example.task_5_1.entity.User;
import org.example.task_5_1.sevice.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminRestController {
    private final UserService userService;

    public record RoleDto(Long id, String name) {}

    public record UserDto(Long id, String username, String firstName, String lastName, Integer age, String email, List<String> roles) {}

    public record UserUpsertDto(Long id, String username, String firstName, String lastName, Integer age, String email, String password, List<String> roleNames) {}

    @GetMapping("/roles")
    public List<RoleDto> getRoles() {
        return userService.getAllRoles().stream()
                .map(r -> new RoleDto(r.getId(), r.getName()))
                .toList();
    }

    @GetMapping("/users")
    public List<UserDto> getUser() {
        return userService.getAll().stream().map(this::toDto).toList();
    }

    @GetMapping("/users/{id}")
    public UserDto getUser(@PathVariable Long id) {
        User u = userService.getById(id);
        if(u == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        return toDto(u);
    }

    @PostMapping("/users")
    public UserDto create(@RequestBody UserUpsertDto dto) {
        User u = new User();
        apply(u, dto);
        userService.save(u, dto.roleNames());
        return toDto(userService.findByUsername(u.getUsername()));
    }

    @PutMapping("/users/{id}")
    public UserDto update(@PathVariable Long id, @RequestBody UserUpsertDto dto) {
        User existing = userService.getById(id);
        if(existing == null) throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

        applyForUpdate(existing, dto);
        apply(existing, new UserUpsertDto(id, dto.username(), dto.firstName(), dto.lastName(), dto.age(), dto.email(), dto.password(), dto.roleNames()));
        userService.save(existing, dto.roleNames());
        return toDto(userService.getById(id));
    }

    @DeleteMapping("/users/{id}")
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }

    private UserDto toDto(User u) {
        var roles = u.getRoles().stream().map(Role::getName).toList();
        return new UserDto(u.getId(), u.getUsername(), u.getFirstName(), u.getLastName(), u.getAge(), u.getEmail(), roles);
    }

    private void apply(User u, UserUpsertDto dto) {
        u.setId(dto.id());
        u.setUsername(dto.username());
        u.setFirstName(dto.firstName());
        u.setLastName(dto.lastName());
        u.setAge(dto.age());
        u.setEmail(dto.email());
        u.setPassword(dto.password());
    }
    private void applyForUpdate(User u, UserUpsertDto dto) {
        if (dto.username() != null && !dto.username().isBlank()) {
            u.setUsername(dto.username());
        }

        u.setFirstName(dto.firstName());
        u.setLastName(dto.lastName());
        u.setAge(dto.age());
        u.setEmail(dto.email());

        if (dto.password() != null && !dto.password().isBlank()) {
            u.setPassword(dto.password());
        }
    }
}
