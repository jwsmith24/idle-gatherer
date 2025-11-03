package dev.jake.backend.player.skill;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/player/{playerId}/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService skillService;

    private SkillDto toDto(Skill skill) {
        return new SkillDto(
                skill.getId(), skill.getName(), skill.getXp(), skill.getLevel()
        );
    }

    @GetMapping
    public ResponseEntity<List<SkillDto>> getPlayerSkills(@PathVariable Integer playerId) {

        List<SkillDto> skills = skillService.getPlayerSkills(playerId)
                .stream().map(this::toDto).toList();

        return ResponseEntity.ok(skills);

    }

    @PatchMapping("/{skillId}")
    public ResponseEntity<SkillDto> addXpToSkill(@PathVariable Integer playerId,
                                                 @PathVariable Integer skillId,
                                                 @Valid @RequestBody UpdateSkillXpRequest request) {

        SkillDto updated = toDto(skillService.updateSkillXp(playerId, skillId, request));

        return ResponseEntity.ok(updated);

    }
}
