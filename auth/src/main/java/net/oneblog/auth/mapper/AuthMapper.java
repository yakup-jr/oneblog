package net.oneblog.auth.mapper;

import net.oneblog.auth.entity.AuthEntity;
import net.oneblog.auth.models.AuthModel;
import net.oneblog.auth.models.BasicRegistrationRequestModel;
import net.oneblog.auth.models.GoogleRegistrationRequestModel;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.ReportingPolicy;

@Mapper(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE, componentModel =
    MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.WARN)
public interface AuthMapper {

    AuthEntity map(AuthModel model);

    AuthModel map(AuthEntity entity);

    AuthEntity map(GoogleRegistrationRequestModel googleRegistrationModel);

    AuthEntity map(BasicRegistrationRequestModel basicRegistrationModel);

}
