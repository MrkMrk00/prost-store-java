package de.unibamberg.dsam.group6.prost.controller;

import de.unibamberg.dsam.group6.prost.repository.UserRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    private final UserRepository repository;

    public IndexController(UserRepository repo) {
        this.repository = repo;
    }

    @GetMapping("/")
    public String index(Model model) {
        var users = this.repository.findAll();
        model.addAttribute("users", users);
        return "pages/index";
    }
}
