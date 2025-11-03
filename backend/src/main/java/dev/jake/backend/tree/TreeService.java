package dev.jake.backend.tree;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TreeService {

    private final TreeRepo treeRepo;

    public TreeService(TreeRepo treeRepo) {
        this.treeRepo = treeRepo;
    }

    public List<Tree> getAllTrees() {
        return treeRepo.findAll();
    }

    public Tree getTree(Integer id) {
        return treeRepo.findById(id).orElseThrow();
    }


}
