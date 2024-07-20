package com.ohmea.todayrecipe.controller;

import com.ohmea.todayrecipe.dto.product.ProductResponseDTO;
import com.ohmea.todayrecipe.dto.response.ResponseDTO;
import com.ohmea.todayrecipe.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    @Autowired
    private final ProductService productService;

    @GetMapping
    ResponseEntity<ResponseDTO<List<ProductResponseDTO>>> getProducts() {
        List<ProductResponseDTO> response = productService.getProductList();

        return ResponseEntity
                .status(HttpStatus.OK.value())
                .body(new ResponseDTO<List<ProductResponseDTO>>(200, "할인 상품 조회 완료", response));
    }
}
