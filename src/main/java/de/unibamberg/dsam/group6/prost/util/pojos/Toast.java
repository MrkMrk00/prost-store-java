package de.unibamberg.dsam.group6.prost.util.pojos;

import java.io.Serializable;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class provides functionality for informing user about practically anything.
 */
@Data
@AllArgsConstructor
public class Toast implements Serializable {
    public static final String TEMPLATE_ATTRIBUTE_NAME = "__toasts";

    public enum ToastLevel {
        INFO,
        SUCCESS,
        NOTICE,
        ERROR
    }

    private ToastLevel level;
    private String message;

    public static Toast info(@NotNull String message) {
        return new Toast(ToastLevel.INFO, message);
    }

    public static Toast success(@NotNull String message) {
        return new Toast(ToastLevel.SUCCESS, message);
    }

    public static Toast notice(@NotNull String message) {
        return new Toast(ToastLevel.NOTICE, message);
    }

    public static Toast error(@NotNull String message) {
        return new Toast(ToastLevel.ERROR, message);
    }
}
