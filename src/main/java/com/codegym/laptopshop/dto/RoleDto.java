package com.codegym.laptopshop.dto;

import com.codegym.laptopshop.entity.RoleName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {
    private Long id;
    private RoleName name;
    private String description;


}
