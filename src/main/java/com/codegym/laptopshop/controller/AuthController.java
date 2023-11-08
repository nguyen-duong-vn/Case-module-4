package com.codegym.laptopshop.controller;

import com.codegym.laptopshop.service.CategoryService;
import com.codegym.laptopshop.service.ProductService;
import com.codegym.laptopshop.service.RoleService;
import com.codegym.laptopshop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("")
public class AuthController {
    private final UserService userService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final RoleService roleService;

    public AuthController(UserService userService, ProductService productService, CategoryService categoryService, RoleService roleService) {
        this.userService = userService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.roleService = roleService;
    }


    @GetMapping("/login")
    ModelAndView login() {
        ModelAndView modelAndView = new ModelAndView("/login");
//        modelAndView.addObject("categoryList", categoryService.findAll());
//        modelAndView.addObject("roleList", roleService.findAll());
        return modelAndView;
    }

    @GetMapping("/access-denied")
    public ModelAndView accessDenied() {
        ModelAndView modelAndView = new ModelAndView("/notification/error-403");
        return modelAndView;
    }


    @GetMapping("/logout")
    public ModelAndView logout() {
        return new ModelAndView("/notification/logout-successful");
    }

}
