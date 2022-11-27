package de.unibamberg.dsam.group6.prost.controller;

import de.unibamberg.dsam.group6.prost.repository.UserRepository;
import de.unibamberg.dsam.group6.prost.service.UserErrorManager;
import de.unibamberg.dsam.group6.prost.util.Toast;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class IndexController {
    private final UserRepository repository;
    private final UserErrorManager errors;

    @GetMapping("/")
    public String index(Model model) {
        var users = this.repository.findAll();
        model.addAttribute("users", users);
        model.addAttribute(Toast.TEMPLATE_ATTRIBUTE_NAME, this.errors.getToastsAndRemove());
        return "pages/index";
    }

    @GetMapping("/votestuj")
    public String meVotestujNe(Model model) {
        this.errors.addToast(Toast.info("INFO: Tvoje mama"));
        this.errors.addToast(Toast.notice("NOTICE: Tvojemama"));
        this.errors.addToast(Toast.error("ERROR: Tvojemama"));
        model.addAttribute(Toast.TEMPLATE_ATTRIBUTE_NAME, this.errors.getToastsAndRemove());

        return "pages/index";
    }
}
