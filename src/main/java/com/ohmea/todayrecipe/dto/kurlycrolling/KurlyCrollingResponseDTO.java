package com.ohmea.todayrecipe.dto.kurlycrolling;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KurlyCrollingResponseDTO {
    private String imageUrl;
    private String name;
    private String discript;
    private int salePercent;
    private int originalPrice;
    private int salePrice;
}
