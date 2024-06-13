package com.mawe.spring_jwt.service.admin;

import com.mawe.spring_jwt.dto.CategoryDto;
import com.mawe.spring_jwt.dto.ProductDto;
import com.mawe.spring_jwt.model.Category;
import com.mawe.spring_jwt.model.Product;
import com.mawe.spring_jwt.repository.CategoryRepository;
import com.mawe.spring_jwt.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImp implements AdminService {


    private final CategoryRepository categoryRepository;

    private final ProductRepository productRepository;

    @Override
    public Category createCategory(CategoryDto categoryDto){
        Category category = new Category();
        category.setName(categoryDto.getName());
        category.setDescription(categoryDto.getDescription());
        return categoryRepository.save(category);
    }
    @Override
    public List<CategoryDto> getAllCategories(){

        return categoryRepository.findAll().stream().map(Category::getCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProductDto postProduct(ProductDto productDto) throws Exception {
        Product product = new Product();
        product.setImage(productDto.getImage().getBytes());
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());

        Category category = categoryRepository.findById(productDto.getCategoryId()).orElseThrow();
        product.setCategory(category);
        return productRepository.save(product).getDto();
    }

    @Override
    public List<ProductDto> getAllProduct(){
        List<Product> products = productRepository.findAll();
        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }

    @Override
    public List<ProductDto> getAllProductByName(String name){
        List<Product> products = productRepository.findAllByNameContaining(name);
        return products.stream().map(Product::getDto).collect(Collectors.toList());
    }

    public boolean deleteProduct(Long id){
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()){
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }


}
