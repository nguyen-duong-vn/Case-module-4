package com.codegym.laptopshop.controller;

import com.codegym.laptopshop.dto.RoleDto;
import com.codegym.laptopshop.dto.UserDto;
import com.codegym.laptopshop.entity.RoleName;
import com.codegym.laptopshop.service.CategoryService;
import com.codegym.laptopshop.service.ProductService;
import com.codegym.laptopshop.service.RoleService;
import com.codegym.laptopshop.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final RoleService roleService;

    public UserController(UserService userService, ProductService productService, CategoryService categoryService, RoleService roleService) {
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


    @GetMapping("/list")
    public ModelAndView list(){
        ModelAndView modelAndView = new ModelAndView("/user/list");
        setNavView(modelAndView);
        modelAndView.addObject("userList", userService.findAll());
        return modelAndView;
    }
    @GetMapping("/create")
    public ModelAndView create() {
        ModelAndView modelAndView = new ModelAndView("/user/create");
        setNavView(modelAndView);
        modelAndView.addObject("user", new UserDto());
        return modelAndView;
    }
    @PostMapping("/create")
    public ModelAndView createSuccess(UserDto userDto) {
        MultipartFile path = userDto.getPath();
        String fileName = path.getOriginalFilename();
        userDto.setAvatar(fileName);

        userDto.setActivated(true);

        UserDto newUserDto = userService.createAndGetUser(userDto).get();

        ModelAndView modelAndView = new ModelAndView("/user/detail");
        setNavView(modelAndView);
        modelAndView.addObject("user", newUserDto);
        return modelAndView;
    }



    @GetMapping("/detail/{id}")
    public ModelAndView view(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("/user/detail");
        setNavView(modelAndView);
        UserDto userDto = userService.findById(id).get();
        modelAndView.addObject("user", userDto);
        return modelAndView;
    }
    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id){
        UserDto userDto = userService.findById(id).get();
        ModelAndView modelAndView = new ModelAndView("/user/edit");
        setNavView(modelAndView);
        modelAndView.addObject("newUserDto", userDto);
        return modelAndView;
    }


    @PostMapping("/edit")
    public ModelAndView editSuccess(@ModelAttribute("newUserDto") UserDto userDto)  {
        if(userDto.getPath().getSize() != 0) {
            MultipartFile multipartFile = userDto.getPath();
            String fileName = multipartFile.getOriginalFilename();
            userDto.setAvatar(fileName);
        }
        Long id = userDto.getId();
        userService.updateAllData(userDto);
        UserDto newUserDto = userService.findById(userDto.getId()).get();
        ModelAndView modelAndView = new ModelAndView("/user/detail");
        setNavView(modelAndView);
        modelAndView.addObject("user", newUserDto);
        return modelAndView;
    }

    @GetMapping("/remove/{id}")
    public ModelAndView remove(@PathVariable Long id){
        userService.remove(id);
        ModelAndView modelAndView = new ModelAndView("redirect:/login");
        return modelAndView;
    }

    @GetMapping("/layoff/{id}")
    public ModelAndView layoff(@PathVariable Long id){
        userService.remove(id);
        ModelAndView modelAndView = new ModelAndView("redirect:/user/list");
        return modelAndView;
    }


    @GetMapping("/update-role/{id}")
    public ModelAndView update(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("/user/update-role");
        setNavView(modelAndView);
        modelAndView.addObject("user", userService.findById(id).get());
        return modelAndView;
    }

    @PostMapping("/update-role")
    public ModelAndView updateSuccess(@ModelAttribute("user") UserDto userDto)  {
        userService.updateRole(userDto);
        UserDto newUserDto = userService.findById(userDto.getId()).get();
        ModelAndView modelAndView = new ModelAndView("/user/detail");
        setNavView(modelAndView);
        modelAndView.addObject("user", newUserDto);
        return modelAndView;
    }


    @GetMapping("/search")
    public ModelAndView searchName(@RequestParam String name){
        Iterable<UserDto> userDtos = userService.findByFullNameContainingAndActivated(name, true);
        ModelAndView modelAndView = new ModelAndView("/user/search");
        modelAndView.addObject("userList", userDtos);
        setNavView(modelAndView);
        return modelAndView;
    }

    @GetMapping("/search/{name}")
    public ModelAndView search(@PathVariable RoleName name){
        RoleDto roleDto = roleService.findByName(name).get();
        Iterable<UserDto> userDtos = userService.findByActivatedAndRole(true, roleDto);
        ModelAndView modelAndView = new ModelAndView("/user/search");
        modelAndView.addObject("userList", userDtos);
        setNavView(modelAndView);
        return modelAndView;
    }

    @GetMapping("/reset-password")
    public ModelAndView resetPassword()  {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = ((UserDetails) principal).getUsername();
        UserDto userDto = userService.findByUsername(username).get();
        ModelAndView modelAndView = new ModelAndView("/user/reset-password");
        modelAndView.addObject("userDto", userDto);
        return modelAndView;
    }

    @PostMapping("/reset-password")
    public ModelAndView resetPasswordSuccessful(UserDto userDto) {

        userService.updatePassword(userDto);
        ModelAndView modelAndView = new ModelAndView("redirect:/logout");
        return modelAndView;
    }






    @GetMapping("/principal/{id}")
    public ModelAndView viewPrincipal(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("/principal/detail");
        setNavView(modelAndView);
        UserDto userDto = userService.findById(id).get();
        modelAndView.addObject("user", userDto);
        return modelAndView;
    }

    @GetMapping("/principal-edit/{id}")
    public ModelAndView editPrincipal(@PathVariable Long id){
        UserDto userDto = userService.findById(id).get();
        ModelAndView modelAndView = new ModelAndView("/principal/edit");
        setNavView(modelAndView);
        modelAndView.addObject("newUserDto", userDto);
        return modelAndView;
    }


    @PostMapping("/principal-edit")
    public ModelAndView editPrincipalSuccess(@ModelAttribute("newUserDto") UserDto userDto)  {
        if(userDto.getPath().getSize() != 0) {
            MultipartFile multipartFile = userDto.getPath();
            String fileName = multipartFile.getOriginalFilename();
            userDto.setAvatar(fileName);
        }
        Long id = userDto.getId();
        userService.updateAllData(userDto);
        UserDto newUserDto = userService.findById(id).get();
        ModelAndView modelAndView = new ModelAndView("/principal/detail");
        setNavView(modelAndView);
        modelAndView.addObject("user", newUserDto);
        return modelAndView;
    }



}
