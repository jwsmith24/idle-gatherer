package dev.jake.backend.player.dtos;

import jakarta.validation.constraints.NotBlank;

public record CreatePlayerRequest(
        @NotBlank String name
        ) {
}
