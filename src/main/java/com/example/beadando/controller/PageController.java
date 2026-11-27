package com.example.beadando.controller;

import com.example.beadando.repository.GepRepository;
import com.example.beadando.repository.SzoftverRepository;
import com.example.beadando.repository.TelepitesRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    private final GepRepository gepRepository;
    private final SzoftverRepository szoftverRepository;
    private final TelepitesRepository telepitesRepository;

    public PageController(GepRepository gepRepository,
                          SzoftverRepository szoftverRepository,
                          TelepitesRepository telepitesRepository) {
        this.gepRepository = gepRepository;
        this.szoftverRepository = szoftverRepository;
        this.telepitesRepository = telepitesRepository;
    }

    @GetMapping("/adatbazis")
    public String adatbazis(Model model) {
        model.addAttribute("gepek", gepRepository.findAll());
        model.addAttribute("szoftverek", szoftverRepository.findAll());
        model.addAttribute("telepitesek", telepitesRepository.findAll());
        return "adatbazis";
    }

    @GetMapping("/kapcsolat")
    public String kapcsolat() {
        return "kapcsolat";
    }

    @GetMapping("/diagram")
    public String diagram() {
        return "diagram";
    }

    @GetMapping("/rest")
    public String rest() {
        return "rest";
    }

    @GetMapping("/uzenetek")
    public String uzenetek() {
        return "uzenetek";
    }

    @GetMapping("/crud")
    public String crud() {
        return "crud";
    }
}