package com.ucsc.mob_backend.repository;

import com.ucsc.mob_backend.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserData, Integer> {

    Optional<UserData> findByUsername(String username);
}
