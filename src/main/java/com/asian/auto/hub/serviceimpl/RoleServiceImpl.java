package com.asian.auto.hub.serviceimpl;


import com.asian.auto.hub.dto.RoleDto;
import com.asian.auto.hub.exception.ResourceNotFoundException;
import com.asian.auto.hub.model.Role;
import com.asian.auto.hub.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl {

    private final RoleRepository roleRepository;

    public Role createRole(RoleDto dto) {
        if (roleRepository.existsByRole(dto.getName())) {
            throw new RuntimeException("Role already exists: " + dto.getName());
        }
        Role role = new Role();
        role.setRole(dto.getName());
        role.setCreatedBy("system");
        role.setCreatedOn(LocalDateTime.now());
        return roleRepository.save(role);
    }

    public Page<Role> getAllRoles(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdOn").descending());
        return roleRepository.findByDeletedFalse(pageable);
    }

    public List<Role> getAllRolesList() {
        return roleRepository.findByDeletedFalse();
    }

    public Role getRoleById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found with id: " + id));
    }

    public Role updateRole(Long id, RoleDto dto) {
        Role existing = getRoleById(id);
        existing.setRole(dto.getName());
        existing.setUpdatedBy("system");
        existing.setUpdatedOn(LocalDateTime.now());
        return roleRepository.save(existing);
    }

    public void deleteRole(Long id) {
        Role existing = getRoleById(id);
        existing.setDeleted(true);
        existing.setUpdatedBy("system");
        existing.setUpdatedOn(LocalDateTime.now());
        roleRepository.save(existing);
    }
}