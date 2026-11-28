package com.example.beadando.controller;

import com.example.beadando.entity.Uzenet;
import com.example.beadando.repository.GepRepository;
import com.example.beadando.repository.SzoftverRepository;
import com.example.beadando.repository.TelepitesRepository;
import com.example.beadando.repository.UzenetRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PageController {

    private final GepRepository gepRepository;
    private final SzoftverRepository szoftverRepository;
    private final TelepitesRepository telepitesRepository;
    private final UzenetRepository uzenetRepository;

    public PageController(GepRepository gepRepository,
                          SzoftverRepository szoftverRepository,
                          TelepitesRepository telepitesRepository,
                          UzenetRepository uzenetRepository) {
        this.gepRepository = gepRepository;
        this.szoftverRepository = szoftverRepository;
        this.telepitesRepository = telepitesRepository;
        this.uzenetRepository = uzenetRepository;
    }

    @GetMapping("/adatbazis")
    public String adatbazis(Model model) {
        model.addAttribute("gepek", gepRepository.findAll());
        model.addAttribute("szoftverek", szoftverRepository.findAll());
        model.addAttribute("telepitesek", telepitesRepository.findAll());
        return "adatbazis";
    }

    @GetMapping("/kapcsolat")
    public String kapcsolat(Model model) {
        model.addAttribute("kapcsolatUrlap", new KapcsolatUrlap("", "", ""));
        return "kapcsolat";
    }

    @PostMapping("/kapcsolat")
    public String uzenetKuldes(@Valid @ModelAttribute("kapcsolatUrlap") KapcsolatUrlap urlap,
                               BindingResult hibak,
                               RedirectAttributes redirectAttributes) {
        if (hibak.hasErrors()) {
            return "kapcsolat";
        }

        uzenetRepository.save(new Uzenet(
                urlap.nev().strip(), urlap.email(), urlap.szoveg().strip()));
        redirectAttributes.addFlashAttribute("sikeresKuldes", true);
        return "redirect:/kapcsolat";
    }

    @GetMapping("/diagram")
    public String diagram(Model model) {
        model.addAttribute("kategoriaStatisztika",
                telepitesRepository.findTelepitesekKategoriankent());
        return "diagram";
    }

    @GetMapping("/rest")
    public String rest() {
        return "rest";
    }

    @GetMapping("/uzenetek")
    public String uzenetek(Model model) {
        model.addAttribute("uzenetek", uzenetRepository.findAllByOrderByKuldveDescIdDesc());
        return "uzenetek";
    }

    @GetMapping("/crud")
    public String crud() {
        return "crud";
    }

    public record KapcsolatUrlap(
            @NotBlank(message = "Add meg a neved.")
            @Size(max = 100, message = "A név legfeljebb 100 karakter lehet.")
            String nev,

            @NotBlank(message = "Add meg az e-mail-címed.")
            @Email(message = "Adj meg egy érvényes e-mail-címet.")
            @Size(max = 254, message = "Az e-mail-cím legfeljebb 254 karakter lehet.")
            String email,

            @NotBlank(message = "Írd meg az üzeneted.")
            @Size(max = 2000, message = "Az üzenet legfeljebb 2000 karakter lehet.")
            String szoveg
    ) {
    }
}