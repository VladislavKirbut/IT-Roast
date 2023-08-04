package com.trainlab.service.impl;

import com.trainlab.dto.request.RoleRequest;
import com.trainlab.exception.ObjectIsExistException;
import com.trainlab.mapper.RoleMapper;
import com.trainlab.model.Role;
import com.trainlab.repository.RoleRepository;
import com.trainlab.service.RoleService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public List<Role> findAll() {
        List<Role> roles = roleRepository.findAll();
        if (roles.isEmpty()) {
            throw new EntityNotFoundException("Roles not found");
        }
        return roles;
    }

    @Override
    @Transactional
    public Role create(RoleRequest roleRequest) {
        if (roleRepository.existsByRoleName(roleRequest.getRoleName()))
            throw new ObjectIsExistException("The same role already exists");
        Role role = roleMapper.toEntity(roleRequest);
        return roleRepository.saveAndFlush(role);
    }

    @Override
    public Role receiveById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Role not found"));
    }

    @Override
    public Role receiveByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName).orElseThrow(() -> new EntityNotFoundException("Role not found"));
    }

    @Override
    @Transactional
    public Role update(RoleRequest roleRequest, Long id) {
        Optional<Role> role = roleRepository.findById(id);
        if (roleRepository.existsByRoleName(roleRequest.getRoleName()))
            throw new ObjectIsExistException("The same role already exists");
        Role updated = roleMapper.partialUpdateToEntity(roleRequest, role.orElseThrow(() -> new EntityNotFoundException("Role not found")));
        return roleRepository.saveAndFlush(updated);
    }

}
