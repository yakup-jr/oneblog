package net.oneblog.article.controller;

import lombok.AllArgsConstructor;
import net.oneblog.article.links.VoteModelAssembler;
import net.oneblog.article.models.VoteCreateModel;
import net.oneblog.article.models.VoteModel;
import net.oneblog.article.service.VoteService;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/articles")
@AllArgsConstructor
public class VoteController {

    private final VoteService voteService;
    private final VoteModelAssembler assembler;

    @PostMapping("/vote")
    public ResponseEntity<Void> createVote(@RequestBody @Validated VoteCreateModel voteModel) {
        voteService.save(voteModel);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/vote/{voteId}")
    public ResponseEntity<EntityModel<VoteModel>> findVoteByVoteId(@PathVariable Long voteId) {
        VoteModel vote = voteService.findByVoteId(voteId);
        EntityModel<VoteModel> model = assembler.toModel(vote);
        return ResponseEntity.ok(model);
    }

    @GetMapping("/vote/article/{articleId}")
    public ResponseEntity<Set<EntityModel<VoteModel>>> findVotesByArticleId(
        @PathVariable Long articleId) {
        Set<VoteModel> votes = voteService.findByArticleId(articleId);
        Set<EntityModel<VoteModel>> entityModelSet =
            votes.stream().map(assembler::toModel).collect(Collectors.toUnmodifiableSet());
        return ResponseEntity.ok(entityModelSet);
    }

    @GetMapping("/vote/user/{userId}")
    public ResponseEntity<Set<EntityModel<VoteModel>>> findVotesByUserId(
        @PathVariable Long userId) {
        Set<VoteModel> votes = voteService.findByUserId(userId);
        Set<EntityModel<VoteModel>> entityModelSet =
            votes.stream().map(assembler::toModel).collect(Collectors.toUnmodifiableSet());
        return ResponseEntity.ok(entityModelSet);
    }

    @DeleteMapping("/vote/{voteId}")
    public ResponseEntity<Void> deleteVoteByVoteId(@PathVariable Long voteId) {
        voteService.deleteByVoteId(voteId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/vote/user/{userId}")
    public ResponseEntity<Void> deleteVotesByArticleId(@PathVariable Long userId) {
        voteService.deleteByUserId(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/vote/article/{articleId}")
    public ResponseEntity<Void> deleteVotesByUserId(@PathVariable Long articleId) {
        voteService.deleteByArticleId(articleId);
        return ResponseEntity.noContent().build();
    }
}
