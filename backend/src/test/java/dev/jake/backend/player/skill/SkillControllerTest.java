package dev.jake.backend.player.skill;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jake.backend.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SkillController.class)
class SkillControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    SkillService skillService;

    Player testPlayer;
    Skill testSkill;

    @BeforeEach
    void setup() {
        testPlayer = Player.builder().id(1).name("craig").build();

        testSkill = Skill.builder()
                .id(99).name("woodcutting").player(testPlayer).build();
    }

    @Nested
    class getAll {
        @Test
        void shouldAcceptGetRequest() throws Exception {
            mockMvc.perform(get("/api/player/{playerId}/skills", testPlayer.getId()))
                    .andExpect(status().isOk());
        }

        @Test
        void shouldReturnListOfSkills() throws Exception {
            List<Skill> skills = List.of(testSkill);

            when(skillService.getPlayerSkills(testPlayer.getId()))
                    .thenReturn(skills);

            mockMvc.perform(get("/api/player/{playerId}/skills", testPlayer.getId()))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(1));

            verify(skillService).getPlayerSkills(testPlayer.getId());
        }
    }

    @Nested
    class updateSkillXp {
        @Test
        void shouldReturnSkillWithUpdatedXp() throws Exception {
            UpdateSkillXpRequest request = new UpdateSkillXpRequest(100);
            Skill updated = testSkill;
            updated.setXp(120);

            when(skillService.updateSkillXp(testPlayer.getId(), testSkill.getId(), request))
                    .thenReturn(updated);


            mockMvc.perform(patch("/api/player/{playerId}/skills/{skillId}", testPlayer.getId(), testSkill.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            ).andExpect(status().isOk());

            verify(skillService).updateSkillXp(testPlayer.getId(), testSkill.getId(), request);

        }

        @Test
        void shouldReturnBadRequestIfSkillXpIsNull() throws Exception {
            UpdateSkillXpRequest badRequest = new UpdateSkillXpRequest(null);

            mockMvc.perform(patch("/api/player/{playerId}/skills/{skillId}", testPlayer.getId(), testSkill.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(badRequest))
            ).andExpect(status().isBadRequest());
        }


    }

}