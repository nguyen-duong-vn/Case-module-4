package com.codegym.laptopshop.dto;

import com.codegym.laptopshop.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;

    private String fullname;

    private String username;

    private String password;

    private String email;

    private String phone;

    private String address;

    private String avatar;

    private boolean activated;

    private Role role;

    private MultipartFile path;

    private String newPassword;
}
