package dev.stockman.functions.posts;

import jakarta.validation.constraints.NotNull;

public record Post(
        @NotNull Integer id,
        @NotNull String message,
        @NotNull String created
) {
}
