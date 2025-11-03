package dev.jake.backend.player.skill;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateSkillXpRequest(
        @NotNull @Min(1) Integer xpGained
) {
}
