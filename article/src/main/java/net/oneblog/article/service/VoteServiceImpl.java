package net.oneblog.article.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.oneblog.article.entity.VoteEntity;
import net.oneblog.article.exception.VoteAlreadyExistsException;
import net.oneblog.article.exception.VoteNotFoundException;
import net.oneblog.article.mapper.VoteMapper;
import net.oneblog.article.models.ArticleModel;
import net.oneblog.article.models.VoteCreateModel;
import net.oneblog.article.models.VoteModel;
import net.oneblog.article.repository.VoteRepository;
import net.oneblog.user.service.UserService;
import net.oneblog.validationapi.models.ValidatedUserModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class VoteServiceImpl implements VoteService {

    private final VoteRepository voteRepository;
    private final VoteMapper mapper;
    private final ArticleService articleService;
    private final UserService userService;

    @Override
    @Transactional
    public void save(VoteCreateModel model) {
        if (voteRepository.existsByArticleIdAndUserId(model.getArticleId(), model.getUserId())) {
            throw new VoteAlreadyExistsException("vote already exists");
        }

        ArticleModel referenceByArticleId =
            articleService.getReferenceByArticleId(model.getArticleId());
        ValidatedUserModel referenceByUserId = userService.getReferenceByUserId(model.getUserId());

        VoteModel voteModel = new VoteModel(null, referenceByArticleId, referenceByUserId,
            model.getVoteType());

        voteRepository.save(mapper.map(voteModel));
    }

    @Override
    @Transactional(readOnly = true)
    public VoteModel findByVoteId(Long voteId) {
        VoteEntity voteEntity =
            voteRepository.findById(voteId).orElseThrow(() -> new VoteNotFoundException(
                "vote with id %s not found".formatted(voteId)));
        return mapper.map(voteEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByArticleId(Long articleId) {
        return voteRepository.countByArticleId(articleId);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByUserId(Long userId) {
        return voteRepository.coundByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Set<VoteModel> findByArticleId(Long articleId) {
        return voteRepository.findByArticleId(articleId).stream().map(mapper::map).collect(
            Collectors.toSet());
    }

    @Override
    @Transactional(readOnly = true)
    public Set<VoteModel> findByUserId(Long userId) {
        return voteRepository.findByUserId(userId).stream().map(mapper::map)
            .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public void deleteByVoteId(Long voteId) {
        voteRepository.deleteById(voteId);
    }

    @Override
    @Transactional
    public void deleteByUserId(Long userId) {
        voteRepository.deleteByUserId(userId);
    }

    @Override
    @Transactional
    public void deleteByArticleId(Long articleId) {
        voteRepository.deleteByArticleId(articleId);
    }
}
