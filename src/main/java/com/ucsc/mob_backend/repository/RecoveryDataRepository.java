package com.ucsc.mob_backend.repository;

import com.ucsc.mob_backend.entity.RecoveryData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecoveryDataRepository extends JpaRepository<RecoveryData, Integer> {

    RecoveryData findByUserID(String userID);

}
