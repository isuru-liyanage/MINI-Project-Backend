package com.ucsc.mob_backend.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class EventDTO {
    @NotEmpty(message = "Event Name is required")
    private String event_name;
    @NotEmpty(message = "Event Description is required")
    private String event_description;
    private String tag;
    private int count;
    private int event_id;
}
//json
