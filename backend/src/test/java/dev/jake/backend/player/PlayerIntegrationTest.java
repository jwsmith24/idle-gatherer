package dev.jake.backend.player;

import dev.jake.backend.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class PlayerIntegrationTest extends BaseIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String getUrl(String path) {
        return "http://localhost:" + port + path;
    }

    @BeforeEach
    void clean() {
        jdbcTemplate.execute(
                "TRUNCATE TABLE player RESTART IDENTITY CASCADE"
        );
    }

    private PlayerDto createPlayer(String name) {
        CreatePlayerRequest request = new CreatePlayerRequest(name);

        ResponseEntity<PlayerDto> response =
                restTemplate.postForEntity(getUrl("/api/player"), request, PlayerDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();

        return response.getBody();
    }

    @Test
    void createPlayer_returnsCreatedPlayer() {

        PlayerDto created = createPlayer("steve");
        assertThat(created).isNotNull();
        assertThat(created.name()).isEqualTo("steve");
    }

    @Test
    void getPlayerById_shouldReturnPlayer() {
        PlayerDto created = createPlayer("bob");

        ResponseEntity<PlayerDto> getResponse =
                restTemplate.getForEntity(getUrl("/api/player/" + created.id()), PlayerDto.class);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody()).isNotNull();
        assertThat(getResponse.getBody().name()).isEqualTo("bob");
        assertThat(getResponse.getBody().gold()).isEqualTo(0);
    }

    @Test
    void getPlayerById_notFound_returns404() {
        ResponseEntity<PlayerDto> response =
                restTemplate.getForEntity(getUrl("/api/player/999"), PlayerDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void getAllPlayers_returnsList() {
        createPlayer("joe");

        ResponseEntity<List<PlayerDto>> getResponse =
                restTemplate.exchange(
                        getUrl("/api/player"),
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<>() {}
                );

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<PlayerDto> players = getResponse.getBody();
        assertThat(players).isNotEmpty();
        assertThat(players).extracting(PlayerDto::name).contains("joe");
    }


}
