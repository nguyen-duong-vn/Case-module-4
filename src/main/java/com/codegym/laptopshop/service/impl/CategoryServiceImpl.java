package com.codegym.laptopshop.service.impl;

import com.codegym.laptopshop.dto.CategoryDto;
import com.codegym.laptopshop.entity.Category;
import com.codegym.laptopshop.repository.CategoryRepository;
import com.codegym.laptopshop.service.CategoryService;
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
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Iterable<CategoryDto> findAll() {
        Iterable<Category> categories = categoryRepository.findByActivated(true);
        return StreamSupport.stream(categories.spliterator(), true)
                .map(category -> modelMapper.map(category, CategoryDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CategoryDto> findById(Long id) {
        Optional<Category> category = categoryRepository.findById(id);
        return category.map(c -> modelMapper.map(c, CategoryDto.class));
    }


    @Override
    public void save(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        categoryRepository.save(category);
    }

    @Override
    public void remove(Long id) {
        Category category = categoryRepository.findById(id).get();
        category.setActivated(false);
        categoryRepository.save(category);
    }

    @Override
    public Iterable<String> getNameAlLCategory() {
        Iterable<Category> categories = categoryRepository.findByActivated(true);
        List<String> nameAllCategory = new ArrayList<>();
        for (Category c: categories) {
            nameAllCategory.add(c.getName());
        }
        return nameAllCategory;
    }
}
