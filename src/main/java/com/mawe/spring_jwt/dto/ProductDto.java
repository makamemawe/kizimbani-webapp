package com.mawe.spring_jwt.dto;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProductDto{
    private Long id;

    private String name;

    private String description;

    private Long   price;

    private byte[] byteImage;

    private Long categoryId;

    private String categoryName;

    private MultipartFile image;

}
