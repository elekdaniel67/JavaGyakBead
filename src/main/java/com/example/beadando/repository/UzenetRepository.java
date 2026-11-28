package com.example.beadando.repository;

import com.example.beadando.entity.Uzenet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UzenetRepository extends JpaRepository<Uzenet, Long> {
    List<Uzenet> findAllByOrderByKuldveDescIdDesc();
}