package com.example.beadando.controller;

import com.example.beadando.entity.Szoftver;
import com.example.beadando.repository.SzoftverRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/crud")
public class CrudController {

    private final SzoftverRepository szoftverRepository;

    public CrudController(SzoftverRepository szoftverRepository) {
        this.szoftverRepository = szoftverRepository;
    }

    @GetMapping({"", "/{id}/szerkesztes"})
    public String crud(@PathVariable(name = "id", required = false) Integer id, Model model) {
        SzoftverUrlap urlap = new SzoftverUrlap("", "");
        if (id != null) {
            Szoftver szoftver = szoftverKeresese(id);
            urlap = new SzoftverUrlap(szoftver.getNev(), szoftver.getKategoria());
        }

        model.addAttribute("szoftverUrlap", urlap);
        return crudOldal(model, id);
    }

    @PostMapping({"", "/{id}"})
    public String mentes(@PathVariable(name = "id", required = false) Integer id,
                         @Valid @ModelAttribute("szoftverUrlap") SzoftverUrlap urlap,
                         BindingResult hibak,
                         Model model,
                         RedirectAttributes atiranyitas) {
        if (hibak.hasErrors()) {
            return crudOldal(model, id);
        }

        Szoftver szoftver = id == null ? new Szoftver() : szoftverKeresese(id);
        String kategoria = urlap.kategoria();
        szoftver.setNev(urlap.nev().strip());
        szoftver.setKategoria(kategoria == null || kategoria.isBlank() ? null : kategoria.strip());
        szoftverRepository.save(szoftver);

        atiranyitas.addFlashAttribute("crudUzenet", "A szoftvert mentettük.");
        return "redirect:/crud";
    }

    @PostMapping("/{id}/torles")
    public String torles(@PathVariable("id") Integer id, RedirectAttributes atiranyitas) {
        Szoftver szoftver = szoftverKeresese(id);
        try {
            szoftverRepository.delete(szoftver);
            atiranyitas.addFlashAttribute("crudUzenet", "A szoftvert töröltük.");
        } catch (DataIntegrityViolationException e) {
            atiranyitas.addFlashAttribute("crudHiba",
                    "A szoftverhez telepítés tartozik, ezért nem törölhető.");
        }

        return "redirect:/crud";
    }

    private String crudOldal(Model model, Integer id) {
        model.addAttribute("szoftverek", szoftverRepository.findAll(Sort.by("nev", "id")));
        model.addAttribute("szerkesztettId", id);
        return "crud";
    }

    private Szoftver szoftverKeresese(Integer id) {
        return szoftverRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "A szoftver nem található."));
    }

    public record SzoftverUrlap(
            @NotBlank(message = "Add meg a szoftver nevét.")
            @Size(max = 255, message = "A név legfeljebb 255 karakter lehet.")
            String nev,

            @Size(max = 255, message = "A kategória legfeljebb 255 karakter lehet.")
            String kategoria
    ) {
    }
}