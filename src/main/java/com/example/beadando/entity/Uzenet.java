package com.example.beadando.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "uzenet")
public class Uzenet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nev;

    @Column(nullable = false, length = 254)
    private String email;

    @Column(nullable = false, length = 2000)
    private String szoveg;

    @Column(nullable = false, updatable = false)
    private Instant kuldve;

    protected Uzenet() {
    }

    public Uzenet(String nev, String email, String szoveg) {
        this.nev = nev;
        this.email = email;
        this.szoveg = szoveg;
        this.kuldve = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getNev() {
        return nev;
    }

    public String getEmail() {
        return email;
    }

    public String getSzoveg() {
        return szoveg;
    }

    public Instant getKuldve() {
        return kuldve;
    }
}