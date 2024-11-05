package com.ucsc.mob_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class DataSourcingEvents {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int event_id;
    private String event_name;
    private String event_description;
    @Column(columnDefinition = "int default 0")
    private int count;

    public void incrementCount() {
        this.count++;
    }

}
