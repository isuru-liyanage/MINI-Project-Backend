package com.ucsc.mob_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponceDTO {
    private String token;
    private String private_key;
    private String Role;
}
