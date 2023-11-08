package com.codegym.laptopshop.controller;

import com.codegym.laptopshop.dto.CategoryDto;
import com.codegym.laptopshop.dto.ProductDto;
import com.codegym.laptopshop.service.CategoryService;
import com.codegym.laptopshop.service.ProductService;
import com.codegym.laptopshop.service.RoleService;
import com.codegym.laptopshop.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {
    private final UserService userService;
    private final ProductService productService;
    private final CategoryService categoryService;
    private final RoleService roleService;

    public ProductController(UserService userService, ProductService productService, CategoryService categoryService, RoleService roleService) {
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


    //Paging
    @GetMapping("/list/{pageNum}")
    public ModelAndView listByPage(@PathVariable(value = "pageNum") int pageNum,
                                     @RequestParam(value = "sortField", defaultValue = "name") String sortField,
                                     @RequestParam(value = "sortDir", defaultValue = "asc") String sortDir) {
        int pageSize = 5;

        Page<ProductDto> page = productService.findPaginated(pageNum,pageSize,sortField,sortDir);

        List<ProductDto>  productDtoList  = page.getContent();

        ModelAndView modelAndView = new ModelAndView("/product/list");
        setNavView(modelAndView);



        modelAndView.addObject("products", productDtoList);
        modelAndView.addObject("totalPages", page.getTotalPages());
        modelAndView.addObject("currentPage", pageNum);
        modelAndView.addObject("sortField", sortField);
        modelAndView.addObject("sortDir",sortDir);
//        modelAndView.addObject("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        modelAndView.addObject("totalItems", page.getTotalElements());

        return modelAndView;
    }


    @GetMapping("/create")
    public ModelAndView create(){
        ModelAndView modelAndView = new ModelAndView("/product/create");
        setNavView(modelAndView);
        modelAndView.addObject("product", new ProductDto());
        return modelAndView;
    }


    @PostMapping("/create")
    public ModelAndView createSuccess( ProductDto productDto) {
        MultipartFile multipartFile = productDto.getPath();
        String fileName = multipartFile.getOriginalFilename();

        productDto.setAvatar(fileName);
        productDto.setActivated(true);
        ProductDto newDataProduct = productService.createAndGetProduct(productDto).get();
        ModelAndView modelAndView = new ModelAndView("/product/detail");
        setNavView(modelAndView);
        modelAndView.addObject("product", newDataProduct);
        return modelAndView;
    }


    @GetMapping("/detail/{id}")
    public ModelAndView view(@PathVariable Long id){
        ProductDto productDto = productService.findById(id).get();
        ModelAndView modelAndView = new ModelAndView("/product/detail");
        setNavView(modelAndView);
        modelAndView.addObject("product", productDto);
        return modelAndView;
    }

    @GetMapping("/edit/{id}")
    public ModelAndView edit(@PathVariable Long id){
        ModelAndView modelAndView = new ModelAndView("/product/edit");
        setNavView(modelAndView);
        modelAndView.addObject("product", productService.findById(id).get());
        return modelAndView;
    }

    @PostMapping("/edit")
    public ModelAndView editSuccess(@ModelAttribute("product") ProductDto productDto)  {

        if(productDto.getPath().getSize() != 0) {
            MultipartFile multipartFile = productDto.getPath();
            String fileName = multipartFile.getOriginalFilename();
            productDto.setAvatar(fileName);
        }

        Long id = productDto.getId();
        productService.update(productDto);
        ProductDto newDataProduct = productService.findById(id).get();
        ModelAndView modelAndView = new ModelAndView("/product/detail");
        setNavView(modelAndView);
        modelAndView.addObject("product", newDataProduct);
        return modelAndView;
    }

    @GetMapping("/remove/{id}")
    public ModelAndView remove(@PathVariable Long id){
        productService.remove(id);
        ModelAndView modelAndView = new ModelAndView("/notification/remove-successfull");
        return modelAndView;
    }

    // search
    @GetMapping("/search")
    public ModelAndView search(@RequestParam String searchTerm){
        ModelAndView modelAndView = new ModelAndView("/product/search");
        modelAndView.addObject("productListSearch", productService.findByNameContaining(searchTerm.toLowerCase()));
        setNavView(modelAndView);
        return modelAndView;
    }

    @GetMapping("/search/{id}")
    public ModelAndView search(@PathVariable Long id){
        CategoryDto categoryDto = categoryService.findById(id).get();
        Iterable<ProductDto> products = productService.findByCategory(categoryDto);
        ModelAndView modelAndView = new ModelAndView("/product/search");
        modelAndView.addObject("productListSearch", products);
        setNavView(modelAndView);
        return modelAndView;
    }

}
