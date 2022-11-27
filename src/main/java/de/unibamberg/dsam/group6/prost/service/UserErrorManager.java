package de.unibamberg.dsam.group6.prost.service;

import de.unibamberg.dsam.group6.prost.util.Toast;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

@Service
public class UserErrorManager {
    private static final String TOAST_SESSION_KEY = "__errors";
    private static final String ERROR_MAP_SESSION_KEY = "__map_errors";

    private final HttpSession session;

    public UserErrorManager(HttpSession session) {
        this.session = session;
    }

    public List<Toast> getToasts() {
        var errors = this.session.getAttribute(TOAST_SESSION_KEY);
        if (errors == null) {
            var newToasts = new ArrayList<Toast>();
            this.session.setAttribute(TOAST_SESSION_KEY, newToasts);
            return newToasts;
        }
        return (List<Toast>) errors;
    }

    public List<Toast> getToastsAndRemove() {
        var toasts = this.getToasts();
        this.session.setAttribute(TOAST_SESSION_KEY, new ArrayList<Toast>());
        return toasts;
    }

    public void addToast(@NotNull Toast toast) {
        var currentToasts = this.getToasts();
        currentToasts.add(toast);
        this.session.setAttribute(TOAST_SESSION_KEY, currentToasts);
    }

    public void addAllToasts(Iterable<Toast> toasts) {
        toasts.forEach(this::addToast);
    }

    public void injectToasts(Model model) {
        model.addAttribute(Toast.TEMPLATE_ATTRIBUTE_NAME, this.getToastsAndRemove());
    }
}
