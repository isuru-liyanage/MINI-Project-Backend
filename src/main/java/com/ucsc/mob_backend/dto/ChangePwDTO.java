package com.ucsc.mob_backend.dto;

import lombok.Data;

@Data
public class ChangePwDTO {
    String OldPassword;
    String NewPassword;
}
