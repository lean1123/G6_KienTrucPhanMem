package ktpm17ctt.g6.identity.service;

import ktpm17ctt.g6.identity.dto.request.PermissionRequest;
import ktpm17ctt.g6.identity.dto.response.PermissionResponse;

import java.util.List;

public interface PermissionService {
    PermissionResponse create(PermissionRequest request);
    List<PermissionResponse> getAll();
    void delete(String permission);
}
