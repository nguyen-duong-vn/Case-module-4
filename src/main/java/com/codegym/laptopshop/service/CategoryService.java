package com.codegym.laptopshop.service;

import com.codegym.laptopshop.dto.CategoryDto;
import org.springframework.stereotype.Service;

@Service
public interface CategoryService extends GeneralService<CategoryDto>{
    Iterable<String> getNameAlLCategory();
}

