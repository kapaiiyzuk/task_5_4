package org.example.task_5_1.sevice;

import org.example.task_5_1.entity.Role;
import org.example.task_5_1.entity.User;
import org.example.task_5_1.repository.RoleRepository;
import org.example.task_5_1.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    @Transactional(readOnly = true)
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Transactional
    public void save(User user, List<String> roleNames) {
        User dbUser = (user.getId() != null)
                ? userRepository.findById(user.getId()).orElseThrow()
                : null;

        if (roleNames == null || roleNames.isEmpty()) {
            if (dbUser != null) user.setRoles(dbUser.getRoles());
            else user.setRoles(Set.of(getOrCreateRole("ROLE_USER")));
        } else {
            Set<Role> roles = roleNames.stream()
                    .map(this::getOrCreateRole)
                    .collect(java.util.stream.Collectors.toSet());
            user.setRoles(roles);
        }

        boolean isNew = (user.getId() == null);

        if (isNew) {
            if (user.getPassword() == null || user.getPassword().isBlank()) {
                throw new IllegalArgumentException("Password is required for new user");
            }
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        } else {
            if (user.getPassword() == null || user.getPassword().isBlank()) {
                user.setPassword(dbUser.getPassword());
            } else if (!user.getPassword().startsWith("$2")) {
                user.setPassword(passwordEncoder.encode(user.getPassword()));
            }
        }

        userRepository.save(user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
    @Transactional
    protected Role getOrCreateRole(String roleName) {
        return roleRepository.findByName(roleName)
                .orElseGet(() -> roleRepository.save(new Role(roleName)));
    }
}
