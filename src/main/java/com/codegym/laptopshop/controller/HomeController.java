package com.codegym.laptopshop.controller;

import com.codegym.laptopshop.dto.UserDto;
import com.codegym.laptopshop.service.CategoryService;
import com.codegym.laptopshop.service.ProductService;
import com.codegym.laptopshop.service.RoleService;
import com.codegym.laptopshop.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
    private final UserService userService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final RoleService roleService;


    public HomeController(UserService userService, ProductService productService, CategoryService categoryService, RoleService roleService) {
        this.userService = userService;
        this.productService = productService;
        this.categoryService = categoryService;
        this.roleService = roleService;
    }

    private void setNavView(ModelAndView modelAndView) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        modelAndView.addObject("userPrincipal", userService.findByUsername(username).get());
        modelAndView.addObject("categoryList", categoryService.findAll());
        modelAndView.addObject("productList", productService.findAll());
        modelAndView.addObject("roleList", roleService.findAll());
    }

    @GetMapping({"/home", "/"})
    public ModelAndView home() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        UserDto userDto = userService.findByUsername(username).get();
        if (!userDto.isActivated()) {
            return new ModelAndView("/check-email");

        }
        ModelAndView modelAndView = new ModelAndView("/home");
        setNavView(modelAndView);
        return modelAndView;
    }

    @PostMapping("/check-email")
    public ModelAndView reset(@RequestParam("email") String email) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        UserDto userDto = userService.findByUsername(username).get();
        if (userDto.getEmail().equalsIgnoreCase(email)) {
            ModelAndView modelAndView = new ModelAndView("/user/reset-password");
            modelAndView.addObject("userDto", userDto);
            return modelAndView;
        }
        return new ModelAndView("redirect:/login");
    }
}
