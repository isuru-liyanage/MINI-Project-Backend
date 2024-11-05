package com.ucsc.mob_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class SingleLineResponceDTO {
    private String message;

}
