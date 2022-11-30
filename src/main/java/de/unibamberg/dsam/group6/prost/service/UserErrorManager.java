package de.unibamberg.dsam.group6.prost.service;

import de.unibamberg.dsam.group6.prost.util.Toast;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserErrorManager {
    private static final String TOAST_SESSION_KEY = "__errors";
    private static final String ERROR_MAP_SESSION_KEY = "__map_errors";
    public static final String TOAST_TEMPLATE_KEY = "__toasts";
    private final HttpSession session;

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
}
