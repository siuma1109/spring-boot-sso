package com.example.sso.configs;

import java.time.LocalDate;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.sso.role.Role;
import com.example.sso.role.RoleService;
import com.example.sso.user.User;
import com.example.sso.user.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
public class DataConfig implements CommandLineRunner {

    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final RoleService roleService;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        // init roles
        initRoles();
        // init users
        initUsers();
    }

    public void initRoles() {
        if (!roleService.existsByName("ADMIN")) {
            Role admin = Role.builder()
                    .name("ADMIN")
                    .build();

            roleService.save(admin);
        }

        if (!roleService.existsByName("USER")) {
            Role user = Role.builder()
                    .name("USER")
                    .build();

            roleService.save(user);
        }
    }

    public void initUsers() {
        if (!userService.existsByEmail("admin@example.com")) {
            List<Role> adminRoleList = roleService.getAdminRoles();

            User admin = User.builder()
                    .name("Admin")
                    .email("admin@example.com")
                    .password(passwordEncoder.encode("1234"))
                    .dob(LocalDate.of(1980, 1, 1))
                    .roles(adminRoleList)
                    .build();
            userService.save(admin);
        }

        if (!userService.existsByEmail("user@example.com")) {
            List<Role> userRoleList = roleService.getUserRoles();

            User user = User.builder()
                    .name("User")
                    .email("user@example.com")
                    .password(passwordEncoder.encode("1234"))
                    .dob(LocalDate.of(1990, 5, 15))
                    .roles(userRoleList)
                    .build();

            userService.save(user);
        }
    }
}
