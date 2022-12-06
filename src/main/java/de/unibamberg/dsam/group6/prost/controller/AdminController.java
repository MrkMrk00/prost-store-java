package de.unibamberg.dsam.group6.prost.controller;

import static de.unibamberg.dsam.group6.prost.service.AdminActionsProvider.AdminActionInstance;

import de.unibamberg.dsam.group6.prost.service.AdminActionsProvider;
import de.unibamberg.dsam.group6.prost.service.UserErrorManager;
import de.unibamberg.dsam.group6.prost.util.Toast;
import de.unibamberg.dsam.group6.prost.util.exception.BadRequestException;
import de.unibamberg.dsam.group6.prost.util.exception.CallFailedException;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserErrorManager errors;
    private final List<AdminActionInstance> actions;

    public AdminController(UserErrorManager errors, AdminActionsProvider actions) {
        this.errors = errors;
        this.actions = actions.getAnnotatedInstances();
    }

    @GetMapping("")
    public String adminIndex(Model model) {
        model.addAttribute("actions", this.actions);
        model.addAttribute("admin_message", this.errors.getAdminMessageAndRemove());
        return "pages/admin";
    }

    @GetMapping("/action")
    public String runAction(
            @RequestParam(name = "a") Optional<String> action,
            @RequestParam Optional<Boolean> await,
            @RequestParam Optional<String> next)
            throws BadRequestException {
        if (action.isEmpty()) {
            return "redirect:" + next.orElse("/admin");
        }

        var a = action.get().split("::");
        if (a.length != 2) {
            return "redirect:" + next.orElse("/admin");
        }

        var instance = this.actions.stream()
                .filter(i -> i.getInstanceName().equals(a[0]))
                .toList();
        if (instance.size() != 1) {
            return "redirect:" + next.orElse("/admin");
        }

        try {
            if (await.orElse(false)) {
                this.errors.addToast(
                        Toast.info(instance.get(0).callAndReturn(a[1]).get().toString()));
            } else {
                instance.get(0).call(a[1]);
            }
        } catch (InterruptedException | ExecutionException e) {
            this.errors.addToast(Toast.error("Execution failed: ", e.getMessage()));
        } catch (CallFailedException e) {
            throw new BadRequestException(e);
        }
        return "redirect:" + next.orElse("/admin");
    }
}
