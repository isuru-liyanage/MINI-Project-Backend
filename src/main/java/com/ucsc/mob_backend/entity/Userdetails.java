package com.ucsc.mob_backend.entity;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Userdetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String dateOfBirth;
    private String timeOfBirth;
    private String locationOfBirth;
    private String bloodGroup;
    private String sex;
    private String height;
    private String ehanicity;
    private String eyeColour;
    private String useridencrypted;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_id", referencedColumnName = "event_id")
    private DataSourcingEvents dataSourcingEvents;

}
