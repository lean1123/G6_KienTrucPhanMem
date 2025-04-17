package ktpm17ctt.g6.identity.mapper;

import ktpm17ctt.g6.identity.dto.request.UserCreationRequest;
import ktpm17ctt.g6.identity.dto.request.RegistrationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(
            target = "phone",
            source = "phone"
    )
    @Mapping(
            target = "gender",
            source = "gender"
    )
    UserCreationRequest toUserCreationRequest(RegistrationRequest request);
}
