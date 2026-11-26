package com.example.beadando.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/adatbazis")
    public String adatbazis() {
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
