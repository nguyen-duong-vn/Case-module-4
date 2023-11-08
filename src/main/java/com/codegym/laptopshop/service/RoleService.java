package com.codegym.laptopshop.service;

import com.codegym.laptopshop.dto.RoleDto;
import com.codegym.laptopshop.entity.RoleName;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface RoleService extends GeneralService<RoleDto> {
    Optional<RoleDto> findByName(RoleName name);
    Iterable<String> getDescriptionAlLRole();
}
