package dev.jake.backend.player;

import dev.jake.backend.player.dtos.CreatePlayerRequest;
import dev.jake.backend.player.dtos.PlayerDto;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/player")
@CrossOrigin
public class PlayerController {

    private final PlayerService playerService;

    public PlayerController(PlayerService playerService) {
        this.playerService = playerService;
    }

    private PlayerDto toDto(Player player) {
        return new PlayerDto(player.getId(), player.getName(), player.getGold());
    }

    @GetMapping()
    public ResponseEntity<List<PlayerDto>> getAllPlayers() {
        List<PlayerDto> players = playerService.getAllPlayers().stream()
                .map(this::toDto)
                .toList();

        return ResponseEntity.ok(players);

    }

    @GetMapping("/{id}")
    public ResponseEntity<PlayerDto> getPlayer(@PathVariable Integer id) {
        try {
            PlayerDto player = toDto(playerService.getPlayer(id));
            return ResponseEntity.ok(player);
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }

    }

    @PostMapping()
    public ResponseEntity<PlayerDto> createPlayer(@Valid @RequestBody CreatePlayerRequest request) {
        PlayerDto player = toDto(playerService.createPlayer(request));
        URI location = UriComponentsBuilder.fromPath("/api/player/{id}")
                .buildAndExpand(player.id())
                .toUri();

        return ResponseEntity.created(location).body(player);
    }
}
