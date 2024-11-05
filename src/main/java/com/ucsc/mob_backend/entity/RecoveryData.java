package com.ucsc.mob_backend.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class RecoveryData {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String fullName;
    private String dateOfBirth;
    private String mothersMaidenName;
    private String childhoodBestFriendName;
    private String childhoodPetName;
    private String ownQuestion;
    private String ownAnswer;
    @Column(unique = true)
    private String userID;
}
