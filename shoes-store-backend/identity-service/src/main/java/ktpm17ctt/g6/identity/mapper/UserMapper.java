package ktpm17ctt.g6.identity.mapper;

import ktpm17ctt.g6.identity.dto.request.UserCreationRequest;
import ktpm17ctt.g6.identity.dto.request.RegistrationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserCreationRequest toUserCreationRequest(RegistrationRequest request);
}
