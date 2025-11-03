package dev.jake.backend.tree;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.jake.backend.tree.dtos.TreeDto;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TreeController.class)
class TreeControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    TreeService treeService;

    Tree testTree;

    @BeforeEach
    void setup() {
        testTree = Tree.builder()
                .id(99).name("oak").xpValue(100).toughness(5).build();
    }

    @Nested
    class getAllTrees {
        @Test
        void shouldAcceptGetRequest() throws Exception {
            mockMvc.perform(get("/api/resources/trees"))
                    .andExpect(status().isOk());
        }

        @Test
        void shouldReturnListOfTrees() throws Exception {
            List<Tree> trees = List.of(Tree.builder().id(1).name("oak").toughness(5).xpValue(100).build());

            when(treeService.getAllTrees())
                    .thenReturn(trees);

            mockMvc.perform(get("/api/resources/trees"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(1));

            verify(treeService).getAllTrees();
        }
    }

    @Nested
    class getTree {

        @Test
        void shouldAcceptGetRequest() throws Exception {
            when(treeService.getTree(testTree.getId()))
                    .thenReturn(testTree);

            mockMvc.perform(get("/api/resources/trees/{id}", testTree.getId()))
                    .andExpect(status().isOk());
        }

        @Test
        void shouldReturnAnExistingPlayer() throws Exception {
            TreeDto expectedTree = new TreeDto(99, "oak", 100, 5);

            when(treeService.getTree(testTree.getId()))
                    .thenReturn(testTree);

            mockMvc.perform(get("/api/resources/trees/{id}", testTree.getId()))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andExpect(content().json(objectMapper.writeValueAsString(expectedTree)));

            verify(treeService).getTree(testTree.getId());
        }

        @Test
        void shouldReturnNotFoundWhenPlayerDoesNotExist() throws Exception {
            when(treeService.getTree(any(Integer.class)))
                    .thenThrow(new NoSuchElementException("tree does not exist"));

            mockMvc.perform(get("/api/resources/trees/{id}", 5))
                    .andExpect(status().isNotFound());
        }
    }


}