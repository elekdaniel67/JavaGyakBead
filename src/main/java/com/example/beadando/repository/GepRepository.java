package com.example.beadando.repository;

import com.example.beadando.entity.Gep;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GepRepository extends JpaRepository<Gep, Integer> {
}