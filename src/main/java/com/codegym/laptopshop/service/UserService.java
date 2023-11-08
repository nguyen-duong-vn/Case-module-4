package com.codegym.laptopshop.service;

import com.codegym.laptopshop.dto.RoleDto;
import com.codegym.laptopshop.dto.UserDto;
import com.codegym.laptopshop.entity.User;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface UserService extends GeneralService<UserDto> {
    Optional<UserDto> findByUsername(String username);
    Iterable<UserDto> findByActivatedAndRole(Boolean isActivated, RoleDto roleDto);
    void updatePassword(UserDto userDto);

    void updateNewPassword(UserDto userDto);
    void updateRole(UserDto userDto);
    void updateAllData(UserDto userDto);
    Iterable<UserDto> findByFullNameContainingAndActivated(String fullname, Boolean isActivated);
    Optional<UserDto> createAndGetUser(UserDto userDto);

}
