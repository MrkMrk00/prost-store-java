package de.unibamberg.dsam.group6.prost.configuration;

import de.unibamberg.dsam.group6.prost.entity.User;
import de.unibamberg.dsam.group6.prost.repository.UserRepository;
import de.unibamberg.dsam.group6.prost.service.UserErrorManager;
import de.unibamberg.dsam.group6.prost.util.Toast;

import java.security.Principal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice(annotations = Controller.class)
@RequiredArgsConstructor
public class ControllerAdviceSetup {
    private final UserErrorManager errors;
    private final UserRepository userRepository;

    @ModelAttribute(name = UserErrorManager.TOAST_TEMPLATE_KEY)
    public List<Toast> getToasts() {
        return this.errors.getToastsAndRemove();
    }

    @ModelAttribute(name = "user")
    public User getUser() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails user) {
            return this.userRepository.findUserByUsername(user.getUsername()).orElse(null);
        } else {
            return null;
        }
    }
}
