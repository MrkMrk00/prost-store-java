package de.unibamberg.dsam.group6.prost.controller;

import de.unibamberg.dsam.group6.prost.repository.BottlesRepository;
import de.unibamberg.dsam.group6.prost.repository.UserRepository;
import de.unibamberg.dsam.group6.prost.service.UserErrorManager;
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
        return "pages/index";
    }

    @GetMapping("/bottles")
    public String bottleh(Model model) {
        var bottles = this.bottlesRepository.findAll();
        model.addAttribute("bottles", bottles);
        return "pages/bottles";
    }
}
