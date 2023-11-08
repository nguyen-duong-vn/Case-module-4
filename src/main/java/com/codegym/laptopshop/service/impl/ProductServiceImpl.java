package com.codegym.laptopshop.service.impl;

import com.codegym.laptopshop.dto.CategoryDto;
import com.codegym.laptopshop.dto.ProductDto;
import com.codegym.laptopshop.entity.Category;
import com.codegym.laptopshop.entity.Product;
import com.codegym.laptopshop.repository.ProductRepository;
import com.codegym.laptopshop.service.ProductService;
import javax.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }
    @Override
    public Iterable<ProductDto> findAll() {
        Iterable<Product> products = productRepository.findByActivated(true);
        return StreamSupport.stream(products.spliterator(), true)
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<ProductDto> findById(Long id) {
        Product product = productRepository.findById(id).get();
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        return Optional.ofNullable(productDto);
    }


    @Override
    public void save(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        productRepository.save(product);
    }

    @Override
    public void remove(Long id) {
        Product product = productRepository.findById(id).get();
        product.setActivated(false);
        productRepository.save(product);
    }

    @Override
    public Iterable<ProductDto> findByNameContaining(String keyword) {
        Iterable<Product> products = productRepository.findByNameContainingAndActivated(keyword, true);
        return StreamSupport.stream(products.spliterator(), true)
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<ProductDto> findByCategory(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        Iterable<Product> products = productRepository.findByCategory(category);
        return StreamSupport.stream(products.spliterator(), true)
                .map(product -> modelMapper.map(product, ProductDto.class))
                .collect(Collectors.toList());
    }



    @Override
    public Page<ProductDto> findAll(Pageable pageable) {
        Page<Product> entities = productRepository.findAll(pageable);

        Page<ProductDto> productDtos = entities.map(entity -> {
            ProductDto productDto = new ProductDto();
            BeanUtils.copyProperties(entity, productDto);
            return productDto;
        });
        return productDtos;
    }

    @Override
    public Page<ProductDto> findPaginated(int pageNum, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ?
                Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

        Page<Product> products = productRepository.findAll(pageable);

        Page<ProductDto> productsDto = products.map(product -> modelMapper.map(product, ProductDto.class));



        return productsDto;
    }

    @Override
    public void deleteByCategory(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        Iterable<Product> products = productRepository.findByCategory(category);
        for(Product p: products) {
            p.setActivated(false);
        }
    }

    @Override
    public void update(ProductDto productDto) {
        Product product = productRepository.findById(productDto.getId()).get();
        product.setAvatar(productDto.getAvatar());
        product.setName(productDto.getName());
        product.setCategory(productDto.getCategory());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(product.getQuantity());
        productRepository.save(product);
    }

    @Override
    public Optional<ProductDto> createAndGetProduct(ProductDto productDto) {
        Product product = modelMapper.map(productDto, Product.class);
        productRepository.save(product);
        Product newProduct = productRepository.findById(product.getId()).get();
        ProductDto newProductDto = modelMapper.map(newProduct, ProductDto.class);
        return Optional.ofNullable(newProductDto);
    }

    @Override
    public Optional<ProductDto> findByName(String name) {
        Optional<Product> product = productRepository.findByName(name);
        ProductDto productDto = modelMapper.map(product, ProductDto.class);
        return Optional.ofNullable(productDto);
    }
    //    @Override
//    public Page<ProductDto> findAllByFullNameContaining(String fullName, Pageable pageable) {
//        Page<Product> entities = productRepository.findAllByNameContainingAndActivated(fullName, true, pageable);
//        Page<ProductDto> productDtos =
//                entities.map(
//                        entity -> {
//                                ProductDto productDto = new ProductDto();
//                                BeanUtils.copyProperties(entity, productDto);
//                                return productDto;
//                                  }
//                             );
//
//        return productDtos;
//    }
}
