package com.example.beadando.controller;

import com.example.beadando.entity.Szoftver;
import com.example.beadando.repository.SzoftverRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/szoftverek", produces = MediaType.APPLICATION_JSON_VALUE)
public class SzoftverRestController {

    private final SzoftverRepository szoftverRepository;

    public SzoftverRestController(SzoftverRepository szoftverRepository) {
        this.szoftverRepository = szoftverRepository;
    }

    @GetMapping
    public List<Szoftver> listazas() {
        return szoftverRepository.findAll(Sort.by("id"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Szoftver> lekeres(@PathVariable("id") Integer id) {
        return ResponseEntity.of(szoftverRepository.findById(id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Szoftver> letrehozas(@Valid @RequestBody SzoftverAdatok adatok) {
        Szoftver mentett = mentes(new Szoftver(), adatok);
        URI hely = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(mentett.getId())
                .toUri();

        return ResponseEntity.created(hely).body(mentett);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Szoftver> modositas(@PathVariable("id") Integer id,
                                              @Valid @RequestBody SzoftverAdatok adatok) {
        return szoftverRepository.findById(id)
                .map(szoftver -> ResponseEntity.ok(mentes(szoftver, adatok)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> torles(@PathVariable("id") Integer id) {
        var talalat = szoftverRepository.findById(id);
        if (talalat.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        try {
            szoftverRepository.delete(talalat.get());
            return ResponseEntity.noContent().build();
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(409).body(
                    Map.of("hiba", "A szoftverhez telepítés tartozik, ezért nem törölhető."));
        }
    }

    private Szoftver mentes(Szoftver szoftver, SzoftverAdatok adatok) {
        String kategoria = adatok.kategoria();
        szoftver.setNev(adatok.nev().strip());
        szoftver.setKategoria(kategoria == null || kategoria.isBlank() ? null : kategoria.strip());
        return szoftverRepository.save(szoftver);
    }

    public record SzoftverAdatok(
            @NotBlank(message = "Add meg a szoftver nevét.")
            @Size(max = 255, message = "A név legfeljebb 255 karakter lehet.")
            String nev,

            @Size(max = 255, message = "A kategória legfeljebb 255 karakter lehet.")
            String kategoria
    ) {
    }
}