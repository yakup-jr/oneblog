package net.oneblog.user.links;

import net.oneblog.api.dto.UserDto;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * The type User model assembler.
 */
@Component
public class UserModelAssembler
    implements RepresentationModelAssembler<UserDto, EntityModel<UserDto>> {

    private final UserLink userLink;

    /**
     * Instantiates a new User model assembler.
     *
     * @param userLink the user link
     */
    public UserModelAssembler(UserLink userLink) {
        this.userLink = userLink;
    }

    @Override
    @NonNull
    public EntityModel<UserDto> toModel(@NonNull UserDto userDto) {
        return EntityModel.of(userDto,
            userLink.findUserByUserId(userDto.userId()).withSelfRel());
    }
}
