package dev.jake.backend.player.skill;

import dev.jake.backend.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SkillServiceTest {

    @Mock
    SkillRepo skillRepo;

    @InjectMocks
    SkillService skillService;

    private Skill testSkill;
    private Player testPlayer;
    private List<Skill> testSkillsList;

    @BeforeEach
    void setup() {

        testPlayer = Player.builder()
                .id(22)
                .name("craig")
                .build();

        testSkill = Skill.builder()
                .name("woodcutting")
                .id(99)
                .player(testPlayer)
                .build();

        testSkillsList = List.of(testSkill);
    }

    @Test
    void getPlayerSkills_shouldCallRepoAndReturnListOfSkills() {
        int id = testPlayer.getId();

        when(skillRepo.findByPlayerId(id))
                .thenReturn(testSkillsList);

        List<Skill> skills = skillService.getPlayerSkills(id);

        assertInstanceOf(List.class, skills);
        assertEquals(1, skills.size());

        verify(skillRepo).findByPlayerId(id);

    }

    @Nested
    class updateSkillXp {
        @Test
        void shouldApplyUpdatedXpToSkill(){
            UpdateSkillXpRequest request = new UpdateSkillXpRequest(100);
            Skill updatedSkill = testSkill;
            updatedSkill.setXp(updatedSkill.getXp() + request.xpGained());

            when(skillRepo.findById(testSkill.getId()))
                    .thenReturn(Optional.of(testSkill));

            when(skillRepo.save(updatedSkill))
                    .thenReturn(updatedSkill);

            Skill result = skillService.updateSkillXp(testPlayer.getId(), testSkill.getId(), request);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(updatedSkill.getId());
            assertThat(result.getXp()).isEqualTo(updatedSkill.getXp());

            verify(skillRepo).findById(testSkill.getId());

        }

        @Test
        void shouldThrowNotFoundWhenSkillDoesNotExist() {
            UpdateSkillXpRequest request = new UpdateSkillXpRequest(9999);
            when(skillRepo.findById(any(Integer.class)))
                    .thenReturn(Optional.empty());

            assertThrows(NoSuchElementException.class, () -> skillService.updateSkillXp(testPlayer.getId(), testSkill.getId(),request ));

            verify(skillRepo).findById(any(Integer.class));
        }

        @Test
        void shouldThrowForbiddenWhenTryingToUpdateAnotherPlayerSkill() {
            UpdateSkillXpRequest request = new UpdateSkillXpRequest(100);
            Player badPlayer = Player.builder()
                    .id(66)
                    .name("bad guy")
                    .build();

            Skill corruptedSkill = Skill.builder()
                    .id(99)
                    .name("woodcutting")
                    .xp(0)
                    .player(badPlayer)
                    .build();


            when(skillRepo.findById(99))
                    .thenReturn(Optional.of(corruptedSkill));

            assertThrows(ResponseStatusException.class, () -> skillService.updateSkillXp(testPlayer.getId(), corruptedSkill.getId(), request));

            verify(skillRepo).findById(99);
            verify(skillRepo, never()).save(corruptedSkill);
        }
    }

}