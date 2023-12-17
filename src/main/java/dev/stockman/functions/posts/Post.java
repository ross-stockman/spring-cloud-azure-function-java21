package dev.stockman.functions.posts;

import java.time.Instant;

public record Post(
        Integer id,
        String message,
        String created
) {
}
