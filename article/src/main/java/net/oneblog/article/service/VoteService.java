package net.oneblog.article.service;

import net.oneblog.article.models.VoteCreateModel;
import net.oneblog.article.models.VoteModel;

import java.util.Set;

public interface VoteService {

    void save(VoteCreateModel model);

    VoteModel findByVoteId(Long voteId);

    Long countByArticleId(Long articleId);

    Long countByUserId(Long userId);

    Set<VoteModel> findByUserId(Long userId);

    Set<VoteModel> findByArticleId(Long articleId);

    void deleteByVoteId(Long voteId);

    void deleteByUserId(Long userId);

    void deleteByArticleId(Long articleId);

}
