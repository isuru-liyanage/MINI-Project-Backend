package com.ucsc.mob_backend.repository;

import com.ucsc.mob_backend.entity.Userdetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDetailsRepository extends JpaRepository<Userdetails, Integer> {
    List<Userdetails> findAllByUseridencrypted(String user_id_encrypted);
}
