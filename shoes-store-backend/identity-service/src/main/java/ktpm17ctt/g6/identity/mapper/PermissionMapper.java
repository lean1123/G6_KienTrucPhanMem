package ktpm17ctt.g6.identity.mapper;

import ktpm17ctt.g6.identity.dto.request.PermissionRequest;
import ktpm17ctt.g6.identity.dto.response.PermissionResponse;
import ktpm17ctt.g6.identity.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {
    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);
}
