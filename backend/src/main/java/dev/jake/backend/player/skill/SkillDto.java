package dev.jake.backend.player.skill;

public record SkillDto(
        Integer id,
        String name,
        Integer xp,
        Integer level
) {
}
