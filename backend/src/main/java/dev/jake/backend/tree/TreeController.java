package dev.jake.backend.tree;

import dev.jake.backend.tree.dtos.TreeDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/resources/trees")
public class TreeController {

    private final TreeService treeService;

    public TreeController(TreeService treeService) {
        this.treeService = treeService;
    }

    private TreeDto toDto(Tree tree) {
        return new TreeDto(
                tree.getId(),
                tree.getName(),
                tree.getXpValue(),
                tree.getToughness()
        );
    }

    @GetMapping
    public ResponseEntity<List<TreeDto>> getAllTrees() {

        List<TreeDto> trees = treeService.getAllTrees()
                .stream().map(this::toDto).toList();

        return ResponseEntity.ok(trees);

    }

    @GetMapping("/{id}")
    public ResponseEntity<TreeDto> getTree(@PathVariable Integer id) {

        try {
            Tree tree = treeService.getTree(id);

            return ResponseEntity.ok(toDto(tree));

        } catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }

    }

}
