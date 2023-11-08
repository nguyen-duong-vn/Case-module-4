package com.codegym.laptopshop.service.impl;

import com.codegym.laptopshop.dto.RoleDto;
import com.codegym.laptopshop.entity.Role;
import com.codegym.laptopshop.entity.RoleName;
import com.codegym.laptopshop.repository.RoleRepository;
import com.codegym.laptopshop.service.RoleService;
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Optional<RoleDto> findByName(RoleName name) {
        Role role = roleRepository.findByName(name).get();
        return Optional.ofNullable(modelMapper.map(role, RoleDto.class));
    }

    @Override
    public Iterable<String> getDescriptionAlLRole() {
        Iterable<Role> roles = roleRepository.findAll();
        List<String> descriptions = new ArrayList<>();
        for(Role r: roles) {
            descriptions.add(r.getDescription());
        }
        return descriptions;
    }

    @Override
    public Iterable<RoleDto> findAll() {
        Iterable<Role> roles = roleRepository.findAll();
        return StreamSupport.stream(roles.spliterator(), true)
                .map(role -> modelMapper.map(role, RoleDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RoleDto> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public void save(RoleDto roleDto) {

    }

    @Override
    public void remove(Long id) {

    }
}
