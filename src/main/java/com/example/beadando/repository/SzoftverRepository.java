package com.example.beadando.repository;

import com.example.beadando.entity.Szoftver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SzoftverRepository extends JpaRepository<Szoftver, Integer> {
}