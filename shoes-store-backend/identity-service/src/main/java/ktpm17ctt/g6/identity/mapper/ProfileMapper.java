package ktpm17ctt.g6.identity.mapper;

import ktpm17ctt.g6.identity.dto.request.ProfileCreationRequest;
import ktpm17ctt.g6.identity.dto.request.UserCreationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    ProfileCreationRequest toProfileCreationRequest(UserCreationRequest request);
}
