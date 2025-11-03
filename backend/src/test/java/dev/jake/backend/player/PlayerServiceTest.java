package dev.jake.backend.player;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PlayerServiceTest {

    @Mock
    PlayerRepo playerRepo;

    @InjectMocks
    PlayerService playerService;

    private Player testPlayer;
    private List<Player> testList;


    @BeforeEach
    void setup() {
        testPlayer = Player.builder()
                .name("craig")
                .id(99)
                .gold(23)
                .build();

        testList = List.of(testPlayer);
    }

    @Nested
    class getAllPlayers {

        @Test
        void shouldCallRepo() {
            when(playerRepo.findAll())
                    .thenReturn(testList);

            List<Player> players = playerService.getAllPlayers();

            assertInstanceOf(List.class, players);
            assertEquals(1, players.size());

            verify(playerRepo).findAll();
        }

    }

    @Nested
    class getPlayer {

        @Test
        void shouldCallRepoAndReturnPlayer() {
            when(playerRepo.findById(testPlayer.getId()))
                    .thenReturn(Optional.of(testPlayer));

            Player player = playerService.getPlayer(testPlayer.getId());

            assertNotNull(player);
            assertEquals(player.getId(), testPlayer.getId());

            verify(playerRepo).findById(testPlayer.getId());
        }

        @Test
        void shouldThrowIfPlayerDoesNotExist() {
            when(playerRepo.findById(1))
                    .thenReturn(Optional.empty());

            assertThrows(NoSuchElementException.class, () -> playerService.getPlayer(1));

            verify(playerRepo).findById(1);
        }
    }

    @Nested
    class createPlayer {

        @Test
        void shouldCallRepoToSaveNewPlayer() {

            CreatePlayerRequest request = new CreatePlayerRequest("craig");

            when(playerRepo.save(any(Player.class)))
                    .thenReturn(testPlayer);

            Player saved = playerService.createPlayer(request);

            assertEquals(testPlayer.getId(), saved.getId());
        }


    }

}