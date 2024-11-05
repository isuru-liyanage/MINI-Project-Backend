package com.ucsc.mob_backend.repository;

import com.ucsc.mob_backend.entity.DataSourcingEvents;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SourcingEventRepository extends JpaRepository<DataSourcingEvents, Integer> {
}
