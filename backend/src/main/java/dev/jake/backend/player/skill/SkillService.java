package dev.jake.backend.player.skill;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillService {

    private final SkillRepo skillRepo;

    public List<Skill> getPlayerSkills(Integer id) {

        return skillRepo.findByPlayerId(id);

    }

    public Skill updateSkillXp(Integer playerId, Integer skillId, UpdateSkillXpRequest request) {

        Skill skill = skillRepo.findById(skillId).orElseThrow();

        // validate ownership
        if (!playerId.equals(skill.getPlayer().getId())) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Tried to update a skill that does not belong to this player");
        }

        int currentXp = skill.getXp();
        skill.setXp(currentXp + request.xpGained());

        return skillRepo.save(skill);
    }
}
