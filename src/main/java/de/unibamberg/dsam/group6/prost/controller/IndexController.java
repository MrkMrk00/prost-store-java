package de.unibamberg.dsam.group6.prost.controller;

import de.unibamberg.dsam.group6.prost.repository.BottlesRepository;
import de.unibamberg.dsam.group6.prost.repository.CratesRepository;
import de.unibamberg.dsam.group6.prost.repository.UserRepository;
import de.unibamberg.dsam.group6.prost.util.OffsetBasedPageRequest;

import java.security.Principal;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import de.unibamberg.dsam.group6.prost.entity.User;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final UserRepository userRepository;
    private final BottlesRepository bottlesRepository;
    private final CratesRepository cratesRepository;

    @GetMapping("/")
    public String index(Model model) {
        var users = this.userRepository.findAll();
        model.addAttribute("users", users);
        return "pages/index";
    }

    @GetMapping("/bottles")
    public String bottleCatalogue(@RequestParam Optional<Integer> page, Model model) {
        var currentPage = 0;
        if (page.isPresent() && page.get() >= 0) {
            currentPage = page.get();
        }

        var pagable = new OffsetBasedPageRequest(currentPage * 9, 9);
        var bottles = this.bottlesRepository.findAll(pagable);

        model.addAttribute("beverages", bottles.getContent());
        model.addAttribute("max_page", (this.bottlesRepository.count() - 1) / 9);
        model.addAttribute("current_page", currentPage);
        return "pages/bottles";
    }

    @GetMapping("/crates")
    public String cratesCatalogue(@RequestParam Optional<Integer> page, Model model) {
        var currentPage = 0;
        if (page.isPresent() && page.get() >= 0) {
            currentPage = page.get();
        }

        var pagable = new OffsetBasedPageRequest(currentPage * 9, 9);
        var bottles = this.cratesRepository.findAll(pagable);

        model.addAttribute("beverages", bottles.getContent());
        model.addAttribute("max_page", (this.cratesRepository.count() - 1) / 9);
        model.addAttribute("current_page", currentPage);
        return "pages/crates";
    }

    @GetMapping("/user")
    public String showUserSettings(Principal principal, Model model) {
        model.addAttribute("user", this.userRepository.findUserByUsername(principal.getName()).orElseThrow());
        return "pages/user";
    }

    @PostMapping("/user")
    public String updateUserSettings(@ModelAttribute @Valid User user) {
        return "redirect:/user";
    }
}
