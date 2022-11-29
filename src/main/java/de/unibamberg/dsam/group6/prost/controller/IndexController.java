package de.unibamberg.dsam.group6.prost.controller;

import de.unibamberg.dsam.group6.prost.repository.BottlesRepository;
import de.unibamberg.dsam.group6.prost.repository.UserRepository;
import de.unibamberg.dsam.group6.prost.service.UserErrorManager;
import de.unibamberg.dsam.group6.prost.util.Toast;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final UserRepository userRepository;
    private final BottlesRepository bottlesRepository;
    private final UserErrorManager errors;

    @GetMapping("/")
    public String index(Model model) {
        var users = this.userRepository.findAll();
        model.addAttribute("users", users);
        this.errors.injectToasts(model);
        return "pages/index";
    }

    @GetMapping("/votestuj")
    public String meVotestujNe() {
        this.errors.addAllToasts(List.of(
                Toast.success("Tvoje máma je skvělá!"),
                Toast.info("INFO: Tvoje mama"),
                Toast.notice("NOTICE: Tvojemama"),
                Toast.error("ERROR: Tvojemama")));

        return "redirect:/";
    }

    @GetMapping("/bottles")
    public String bottleh(Model model) {
        var bottles = this.bottlesRepository.findAll();
        model.addAttribute("bottles", bottles);
        this.errors.injectToasts(model);
        return "pages/bottles";
    }
}
