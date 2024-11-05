package com.ucsc.mob_backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;

@Data
@Component
public class PrivateInformationDTO {

    @NotEmpty(message = "Full name is required")
    private String fullName;
    @NotEmpty(message = "Date of birth is required")
    private String dateOfBirth;
    @NotEmpty(message = "Mothers maiden name is required")
    private String mothersMaidenName;
    @NotEmpty(message = "Childhood best friend name is required")
    private String childhoodBestFriendName;
    @NotEmpty(message = "Childhood pet name is required")
    private String childhoodPetName;
    @NotEmpty(message = "Own question is required")
    private String ownQuestion;
    @NotEmpty(message = "Own answer is required")
    private String ownAnswer;

}


