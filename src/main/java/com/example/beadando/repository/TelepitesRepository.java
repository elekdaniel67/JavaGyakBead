package com.example.beadando.repository;

import com.example.beadando.entity.Telepites;
import com.example.beadando.entity.TelepitesId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TelepitesRepository extends JpaRepository<Telepites, TelepitesId> {
}