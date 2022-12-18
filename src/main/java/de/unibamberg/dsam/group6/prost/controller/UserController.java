package de.unibamberg.dsam.group6.prost.controller;

import de.unibamberg.dsam.group6.prost.entity.Address;
import de.unibamberg.dsam.group6.prost.entity.User;
import de.unibamberg.dsam.group6.prost.repository.AddressRepository;
import de.unibamberg.dsam.group6.prost.repository.UserRepository;
import de.unibamberg.dsam.group6.prost.service.UserErrorManager;
import de.unibamberg.dsam.group6.prost.util.Toast;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.security.Principal;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;

    private final UserErrorManager errors;

    @GetMapping("")
    public String showUserSettings(Principal principal, Model model) {
        model.addAttribute("address", new Address());
        model.addAttribute("allAddresses", this.addressRepository.findAll());
        model.addAttribute("user", this.userRepository.findUserByUsername(principal.getName()).orElseThrow());
        return "pages/user";
    }

    @PostMapping("")
    public String updateUserSettings(@ModelAttribute @Valid User user) {
        return "redirect:/user";
    }

    @PostMapping("/addAddress")
    public String addUserAddress(@ModelAttribute @Valid Address address, Errors errors) {
        if (errors.hasErrors()) {
            errors.getAllErrors().forEach(e -> {
                this.errors.addToast(Toast.error("%s: %s", (e.getCodes() == null ? "" : e.getCodes()[1]), e.getDefaultMessage()));
            });
        } else {
            var added = this.addressRepository.save(address);
            this.errors.addToast(Toast.success("%s added successfully.", added.toString()));
        }
        return "redirect:/user";
    }
}

