package dev.jake.backend.player.skill;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepo extends JpaRepository<Skill, Integer> {

    List<Skill> findByPlayerId (Integer playerId);
}
