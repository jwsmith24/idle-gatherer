package dev.jake.backend.player;

import dev.jake.backend.player.skill.Skill;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;

    @Builder.Default
    @OneToMany(
            mappedBy = "player", // field name in skill
            cascade = CascadeType.ALL, // propagate persist/remove actions
            orphanRemoval = true, // delete skills if a player is deleted
            fetch = FetchType.LAZY // only load skills when needed otherwise proxy
    )
    private List<Skill> skills = new ArrayList<>();

    @Builder.Default
    @Column(nullable = false)
    private Integer gold =  0;

    @CreationTimestamp
    private Instant createdAt;

    @UpdateTimestamp
    private Instant updatedAt;

    // helpers

    public void addSkill(Skill skill) {
        skills.add(skill);
        skill.setPlayer(this);
    }

    public void removeSkill(Skill skill) {
        skills.remove(skill);
        skill.setPlayer(null);
    }

}
