package ktpm17ctt.g6.identity.mapper;

import ktpm17ctt.g6.identity.dto.request.RoleRequest;
import ktpm17ctt.g6.identity.dto.response.RoleResponse;
import ktpm17ctt.g6.identity.entity.Role;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    @Mapping(target = "permissions", ignore = true)
    Role toRole(RoleRequest request);

    RoleResponse toRoleResponse(Role role);
}
