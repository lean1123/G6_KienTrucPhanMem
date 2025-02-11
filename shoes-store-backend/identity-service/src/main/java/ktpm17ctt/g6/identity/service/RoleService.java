package ktpm17ctt.g6.identity.service;

import ktpm17ctt.g6.identity.dto.request.RoleRequest;
import ktpm17ctt.g6.identity.dto.response.RoleResponse;

import java.util.List;

public interface RoleService {
    RoleResponse create(RoleRequest request);
    List<RoleResponse> getAll();
    void delete(String role);
}
