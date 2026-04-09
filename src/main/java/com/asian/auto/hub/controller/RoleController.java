package com.asian.auto.hub.controller;



import com.asian.auto.hub.apiresponse.ApiResponse;
import com.asian.auto.hub.dto.PagedResponse;
import com.asian.auto.hub.dto.RoleDto;
import com.asian.auto.hub.model.Role;
import com.asian.auto.hub.serviceimpl.RoleServiceImpl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleServiceImpl roleService;

    @PostMapping
    public ResponseEntity<ApiResponse<Role>> create(
            @RequestBody RoleDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.created("Role created successfully",
                        roleService.createRole(dto)));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<PagedResponse<Role>>> getAll(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<Role> result = roleService.getAllRoles(page, size);
        return ResponseEntity.ok(ApiResponse.paged(result));
    }

    @GetMapping("/all")
    public ResponseEntity<ApiResponse<List<Role>>> getAllList() {
        return ResponseEntity.ok(ApiResponse.success(roleService.getAllRolesList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Role>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(roleService.getRoleById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Role>> update(
            @PathVariable Long id,
            @RequestBody RoleDto dto,
            @RequestHeader("X-Updated-By") String updatedBy) {
        return ResponseEntity.ok(ApiResponse.success("Role updated successfully",
                roleService.updateRole(id, dto, updatedBy)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            @RequestHeader("X-Updated-By") String deletedBy) {
        roleService.deleteRole(id, deletedBy);
        return ResponseEntity.ok(ApiResponse.deleted("Role deleted successfully"));
    }
}