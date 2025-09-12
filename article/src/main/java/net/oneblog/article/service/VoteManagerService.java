package net.oneblog.article.service;

import net.oneblog.api.interfaces.VoteType;

public interface VoteManagerService {

    Long countByArticleIdAndVoteType(Long articleId, VoteType voteType);

}
