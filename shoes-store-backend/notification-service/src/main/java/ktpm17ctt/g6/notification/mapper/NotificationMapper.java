package ktpm17ctt.g6.notification.mapper;

import org.mapstruct.Mapper;

import ktpm17ctt.g6.notification.dto.request.NotificationRequestDTO;
import ktpm17ctt.g6.notification.dto.response.NotificationResponseDTO;
import ktpm17ctt.g6.notification.entity.Notification;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    Notification toEntity(NotificationRequestDTO dto);
    NotificationResponseDTO toResponseDTO(Notification notification);
}
