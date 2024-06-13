package com.mawe.spring_jwt.service.admin;

import com.mawe.spring_jwt.dto.CategoryDto;
import com.mawe.spring_jwt.dto.ProductDto;
import com.mawe.spring_jwt.model.Category;
import com.mawe.spring_jwt.model.Product;

import java.io.IOException;
import java.util.List;

public interface AdminService {

    Category createCategory(CategoryDto categoryDto);

    List<CategoryDto> getAllCategories();

    ProductDto postProduct(ProductDto productDto) throws Exception;

    List<ProductDto> getAllProduct();


    List<ProductDto> getAllProductByName(String name);

    boolean deleteProduct(Long productId);
}
