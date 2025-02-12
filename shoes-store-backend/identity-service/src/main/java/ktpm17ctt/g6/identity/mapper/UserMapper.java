package ktpm17ctt.g6.identity.mapper;

import ktpm17ctt.g6.identity.dto.request.UserCreationRequest;
import ktpm17ctt.g6.identity.dto.request.UserUpdateRequest;
import ktpm17ctt.g6.identity.dto.response.UserResponse;
import ktpm17ctt.g6.identity.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    UserResponse toUserResponse(User user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget User user, UserUpdateRequest request);
}
