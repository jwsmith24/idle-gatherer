package dev.jake.backend.player;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.NoSuchElementException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PlayerController.class)
class PlayerControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    PlayerService playerService;

    Player testPlayer;

    @BeforeEach
    void setup() {
        testPlayer = Player.builder()
                .id(99).name("craig").gold(23).build();
    }

    @Nested
    class getAll {
        @Test
        void shouldAcceptGetRequest() throws Exception {
            mockMvc.perform(get("/api/player"))
                    .andExpect(status().isOk());
        }

        @Test
        void shouldReturnListOfPlayers() throws Exception {
            List<Player> players = List.of(Player.builder().id(1).name("craig").gold((22)).build());

            when(playerService.getAllPlayers())
                    .thenReturn(players);

            mockMvc.perform(get("/api/player"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(1));

            verify(playerService).getAllPlayers();
        }
    }

    @Nested
    class getPlayer {

        @Test
        void shouldAcceptGetRequest() throws Exception {
            when(playerService.getPlayer(testPlayer.getId()))
                    .thenReturn(testPlayer);

            mockMvc.perform(get("/api/player/{id}", testPlayer.getId()))
                    .andExpect(status().isOk());
        }

        @Test
        void shouldReturnAnExistingPlayer() throws Exception {
            when(playerService.getPlayer(testPlayer.getId()))
                    .thenReturn(testPlayer);

            mockMvc.perform(get("/api/player/{id}", testPlayer.getId()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(jsonPath("$.name").value(testPlayer.getName()))
                    .andExpect(jsonPath("$.id").value(testPlayer.getId()))
                    .andExpect(jsonPath("$.gold").value(testPlayer.getGold()));

            verify(playerService).getPlayer(testPlayer.getId());
        }

        @Test
        void shouldReturnNotFoundWhenPlayerDoesNotExist() throws Exception {
            when(playerService.getPlayer(any(Integer.class)))
                    .thenThrow(new NoSuchElementException("player does not exist"));

            mockMvc.perform(get("/api/player/{id}", 5))
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class createPlayer {
        private CreatePlayerRequest request;
        private PlayerDto expectedResponse;

        @BeforeEach
        void setup() {
            request = new CreatePlayerRequest("craig");
            expectedResponse = new PlayerDto(99, "craig", 23);
        }
        @Test
        void shouldAcceptPostRequest() throws Exception {
            when(playerService.createPlayer(request))
                    .thenReturn(testPlayer);

            mockMvc.perform(post("/api/player")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            ).andExpect(status().isCreated());
        }
        @Test
        void shouldCreateNewPlayer() throws Exception {
            when(playerService.createPlayer(request))
                    .thenReturn(testPlayer);

            mockMvc.perform(post("/api/player")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request))
            ).andExpect(status().isCreated())
                    .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
        }

        @Test
        void shouldReturnBadRequestIfNameIsNull() throws Exception {
            CreatePlayerRequest badRequest = new CreatePlayerRequest(null);

            mockMvc.perform(post("/api/player")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(badRequest))
            ).andExpect(status().isBadRequest());

        }

    }


}