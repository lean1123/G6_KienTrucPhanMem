package ktpm17ctt.g6.identity.mapper;

import ktpm17ctt.g6.identity.dto.request.UserCreationRequest;
import ktpm17ctt.g6.identity.dto.request.UserUpdateRequest;
import ktpm17ctt.g6.identity.dto.response.UserResponse;
import ktpm17ctt.g6.identity.entity.Account;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    Account toUser(UserCreationRequest request);

    UserResponse toUserResponse(Account user);

    @Mapping(target = "roles", ignore = true)
    void updateUser(@MappingTarget Account user, UserUpdateRequest request);
}
