package com.ohmea.todayrecipe.dto.detectimage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DetectImageResponseDTO {
    private List<String> ingredients = new ArrayList<>();
}
