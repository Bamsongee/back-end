package com.ohmea.todayrecipe.service;

import com.ohmea.todayrecipe.dto.product.ProductResponseDTO;
import com.ohmea.todayrecipe.dto.response.ResponseDTO;
import com.ohmea.todayrecipe.entity.ProductEntity;
import com.ohmea.todayrecipe.repository.ProductRepository;
import com.ohmea.todayrecipe.util.CsvReader;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    @Autowired private final ProductRepository productRepository;
    @Autowired private CsvReader csvReader;
    private static final String CSV_FILE_PATH = "static/product_entity.csv";

    @PostConstruct
    public void init() {
        List<ProductEntity> products = csvReader.readProductsFromCsv(CSV_FILE_PATH);
        productRepository.saveAll(products);
    }

    /**
     * 할인 상품 조회
     */
    public List<ProductResponseDTO> getProductList() {
        List<ProductEntity> productEntityList = productRepository.findAll();

        List<ProductResponseDTO> productResponseDTOList = new ArrayList<>();
        productEntityList.forEach(entity -> {
            productResponseDTOList.add(ProductResponseDTO.toDto(entity));
        });

        return productResponseDTOList;
    }
}
