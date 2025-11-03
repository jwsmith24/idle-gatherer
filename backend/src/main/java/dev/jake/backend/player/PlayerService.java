package dev.jake.backend.player;

import dev.jake.backend.player.skill.Skill;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {

    private final PlayerRepo playerRepo;

    public PlayerService(PlayerRepo playerRepo) {
        this.playerRepo = playerRepo;
    }

    public List<Player> getAllPlayers() {
        return playerRepo.findAll();
    }

    public Player getPlayer(Integer id) {
        return playerRepo.findById(id).orElseThrow();
    }

    public Player createPlayer(CreatePlayerRequest request) {
        Player player = Player.builder().name(request.name()).build();
        return playerRepo.save(player);
    }
}
