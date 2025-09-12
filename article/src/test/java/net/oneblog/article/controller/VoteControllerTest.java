package net.oneblog.article.controller;

import net.oneblog.article.exception.VoteNotFoundException;
import net.oneblog.article.links.VoteModelAssembler;
import net.oneblog.article.models.VoteCreateModel;
import net.oneblog.article.models.VoteModel;
import net.oneblog.article.service.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteControllerTest {

    @Mock
    private VoteService voteService;

    @Mock
    private VoteModelAssembler assembler;

    @InjectMocks
    private VoteController voteController;

    private VoteCreateModel voteCreateModel;
    private VoteModel voteModel;
    private EntityModel<VoteModel> entityModel;

    @BeforeEach
    void setUp() {
        voteCreateModel = VoteCreateModel.builder()
            .articleId(1L)
            .userId(1L)
            .build();

        voteModel = VoteModel.builder()
            .voteId(1L)
            .build();

        entityModel = EntityModel.of(voteModel);
    }

    @Test
    void createVote_ShouldCallService() {
        doNothing().when(voteService).save(any(VoteCreateModel.class));

        voteController.createVote(voteCreateModel);

        verify(voteService).save(voteCreateModel);
    }

    @Test
    void findVoteByVoteId_ShouldReturnVote_WhenVoteExists() {
        when(voteService.findByVoteId(1L)).thenReturn(voteModel);
        when(assembler.toModel(voteModel)).thenReturn(entityModel);

        ResponseEntity<EntityModel<VoteModel>> result = voteController.findVoteByVoteId(1L);

        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertEquals(entityModel, result.getBody());
        verify(voteService).findByVoteId(1L);
        verify(assembler).toModel(voteModel);
    }

    @Test
    void findVoteByVoteId_ShouldThrowException_WhenVoteNotFound() {
        when(voteService.findByVoteId(1L)).thenThrow(new VoteNotFoundException("Vote not found"));

        assertThrows(VoteNotFoundException.class, () -> voteController.findVoteByVoteId(1L));

        verify(voteService).findByVoteId(1L);
        verifyNoInteractions(assembler);
    }

    @Test
    void findVotesByArticleId_ShouldReturnVotes() {
        Set<VoteModel> votes = Set.of(voteModel);
        when(voteService.findByArticleId(1L)).thenReturn(votes);
        when(assembler.toModel(voteModel)).thenReturn(entityModel);

        ResponseEntity<Set<EntityModel<VoteModel>>> result =
            voteController.findVotesByArticleId(1L);

        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        verify(voteService).findByArticleId(1L);
        verify(assembler).toModel(voteModel);
    }

    @Test
    void findVotesByArticleId_ShouldReturnEmptySet_WhenNoVotes() {
        when(voteService.findByArticleId(1L)).thenReturn(Set.of());

        ResponseEntity<Set<EntityModel<VoteModel>>> result =
            voteController.findVotesByArticleId(1L);

        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertNotNull(result.getBody());
        assertTrue(result.getBody().isEmpty());
        verify(voteService).findByArticleId(1L);
        verifyNoInteractions(assembler);
    }

    @Test
    void findVotesByUserId_ShouldReturnVotes() {
        Set<VoteModel> votes = Set.of(voteModel);
        when(voteService.findByUserId(1L)).thenReturn(votes);
        when(assembler.toModel(voteModel)).thenReturn(entityModel);

        ResponseEntity<Set<EntityModel<VoteModel>>> result = voteController.findVotesByUserId(1L);

        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertNotNull(result.getBody());
        assertEquals(1, result.getBody().size());
        verify(voteService).findByUserId(1L);
        verify(assembler).toModel(voteModel);
    }

    @Test
    void findVotesByUserId_ShouldReturnEmptySet_WhenNoVotes() {
        when(voteService.findByUserId(1L)).thenReturn(Set.of());

        ResponseEntity<Set<EntityModel<VoteModel>>> result = voteController.findVotesByUserId(1L);

        assertEquals(HttpStatusCode.valueOf(200), result.getStatusCode());
        assertNotNull(result.getBody());
        verify(voteService).findByUserId(1L);
        verifyNoInteractions(assembler);
    }

    @Test
    void deleteVoteByVoteId_ShouldReturnNoContent() {
        doNothing().when(voteService).deleteByVoteId(1L);

        ResponseEntity<Void> result = voteController.deleteVoteByVoteId(1L);

        assertEquals(HttpStatusCode.valueOf(204), result.getStatusCode());
        verify(voteService).deleteByVoteId(1L);
    }

    @Test
    void deleteVotesByUserId_ShouldReturnNoContent() {
        doNothing().when(voteService).deleteByUserId(1L);

        ResponseEntity<Void> result = voteController.deleteVotesByArticleId(1L);

        assertEquals(HttpStatusCode.valueOf(204), result.getStatusCode());
        verify(voteService).deleteByUserId(1L);
    }

    @Test
    void deleteVotesByArticleId_ShouldReturnNoContent() {
        doNothing().when(voteService).deleteByArticleId(1L);

        ResponseEntity<Void> result = voteController.deleteVotesByUserId(1L);

        assertEquals(HttpStatusCode.valueOf(204), result.getStatusCode());
        verify(voteService).deleteByArticleId(1L);
    }
}
