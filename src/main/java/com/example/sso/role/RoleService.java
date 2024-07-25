package com.example.sso.role;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.example.sso.exceptions.ValidationException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class RoleService {

    private final RoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public Role getOrCreate(Role role, String name) {
        return roleRepository.findByName(name)
                .orElseGet(() -> roleRepository.save(role));
    }

    public List<Role> findAllById(List<Integer> ids) {
        return roleRepository.findAllById(ids);
    }

    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }

    public Role addNewRole(NewRoleRequest newRoleRequest) {
        Map<String, String> errors = new HashMap<>();
        Optional<Role> roleByName = roleRepository.findByName(newRoleRequest.getName());

        if (roleByName.isPresent()) {
            errors.put("name", "Name is already taken");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        Role role = new Role();
        role.setName(newRoleRequest.getName());
        role.setDescription(newRoleRequest.getDescription());

        return roleRepository.save(role);
    }

    public Role save(Role role) {
        Map<String, String> errors = new HashMap<>();
        Optional<Role> roleByName = roleRepository.findByName(role.getName());

        if (roleByName.isPresent()) {
            errors.put("name", "Name is already taken");
        }

        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }

        return roleRepository.save(role);
    }

    public List<Role> getUserRoles() {
        List<String> roleNames = new ArrayList<>();
        roleNames.add("USER");
        return roleRepository.findByNameIn(roleNames);
    }

    public List<Role> getAdminRoles() {
        List<String> roleNames = new ArrayList<>();
        roleNames.add("ADMIN");
        return roleRepository.findByNameIn(roleNames);
    }
}
