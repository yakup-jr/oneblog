package net.oneblog.article.service;

import lombok.AllArgsConstructor;
import net.oneblog.api.interfaces.VoteType;
import net.oneblog.article.repository.VoteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class VoteManagerServiceImpl implements VoteManagerService {

    private final VoteRepository voteRepository;

    @Override
    @Transactional(readOnly = true)
    public Long countByArticleIdAndVoteType(Long articleId, VoteType voteType) {
        return voteRepository.countByArticleIdAndType(articleId, voteType);
    }
}
