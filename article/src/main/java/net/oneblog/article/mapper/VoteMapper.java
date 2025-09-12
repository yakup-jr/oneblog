package net.oneblog.article.mapper;

import net.oneblog.article.entity.VoteEntity;
import net.oneblog.article.models.VoteCreateModel;
import net.oneblog.article.models.VoteModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING,
    nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
    unmappedTargetPolicy = ReportingPolicy.WARN)
public interface VoteMapper {

    VoteModel map(VoteEntity voteEntity);

    VoteEntity map(VoteModel voteModel);

    VoteEntity map(VoteCreateModel voteCreateModel);
}
