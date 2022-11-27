package de.unibamberg.dsam.group6.prost.controller;

import de.unibamberg.dsam.group6.prost.entity.User;
import de.unibamberg.dsam.group6.prost.repository.UserRepository;
import de.unibamberg.dsam.group6.prost.service.UserErrorManager;
import de.unibamberg.dsam.group6.prost.util.Toast;
import java.security.Principal;
import java.time.LocalDate;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class AuthController {
    private final UserErrorManager errors;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final Validator validator;

    @GetMapping("/whoami")
    @ResponseBody
    public String whoami(Principal principal) {
        return principal == null ? "not logged in" : principal.getName();
    }

    @GetMapping("/login")
    public String login() {
        return "pages/login";
    }

    @GetMapping("/register")
    public String registerPage(Principal principal, Model model) {
        model.addAttribute(Toast.TEMPLATE_ATTRIBUTE_NAME, this.errors.getToastsAndRemove());

        if (principal != null) {
            return "redirect:/";
        }
        return "pages/register";
    }

    @PostMapping("/register")
    public String register(
            HttpServletRequest req,
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String passwordCheck,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthday) {
        // Check if passwords match
        if (!password.equals(passwordCheck)) {
            this.errors.addToast(Toast.error("Passwords didn't match!"));
            return "redirect:/register";
        }

        // Create user object
        var user = User.builder()
                .username(username)
                .password(this.passwordEncoder.encode(password))
                .birthday(birthday)
                .build();

        // Native Bean Validation constraint checking
        var res = this.validator.validate(user);
        if (!res.isEmpty()) {
            res.forEach(err -> this.errors.addToast(Toast.error(err.getMessage())));
            res.forEach(System.out::println);
            return "redirect:/register";
        }

        // persist user and try to log in
        this.userRepo.saveAndFlush(user);
        try {
            req.login(username, password);
        } catch (ServletException e) {
            this.errors.addToast(Toast.notice("Failed to log in :/"));
        }

        return "redirect:/";
    }
}
