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

    @Transactional
    public void save(User user) {
        User dbUser = (user.getId() != null)
                ? userRepository.findById(user.getId()).orElseThrow()
                : null;

        if (user.getId() != null) {
            if (user.getRoles() == null || user.getRoles().isEmpty()) {
                user.setRoles(dbUser.getRoles());
            }
        } else {
            if (user.getRoles() == null || user.getRoles().isEmpty()) {
                user.setRoles(Set.of(getOrCreateRole("ROLE_USER")));
            }
        }

        if (user.getId() != null && (user.getPassword() == null || user.getPassword().isBlank())) {
            user.setPassword(dbUser.getPassword());
        } else if (user.getPassword() != null && !user.getPassword().startsWith("$2")) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
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
