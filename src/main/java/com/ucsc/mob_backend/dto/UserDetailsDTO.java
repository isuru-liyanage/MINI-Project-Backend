package com.ucsc.mob_backend.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDetailsDTO {
    @NotEmpty(message = "Date of Birth is required")
    private String dateOfBirth;
    @NotEmpty(message = "Time of Birth is required")
    private String timeOfBirth;
    @NotEmpty(message = "Location of Birth is required")
    private String locationOfBirth;
    @NotEmpty(message = "Blood Group is required")
    private String bloodGroup;
    @NotEmpty(message = "sex is required")
    private String sex;
    @NotEmpty(message = "Height is required")
    private String height;
    @NotEmpty(message = "Ethnicity is required")
    private String ehanicity;
    @NotEmpty(message = "Eye Colour is required")
    private String eyeColour;
    @NotNull(message = "Event ID is required")
    private int event_id;
}


