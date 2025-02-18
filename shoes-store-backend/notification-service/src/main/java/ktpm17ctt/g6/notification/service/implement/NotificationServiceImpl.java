package ktpm17ctt.g6.notification.service.implement;

import org.springframework.stereotype.Service;

import ktpm17ctt.g6.notification.dto.request.NotificationRequestDTO;
import ktpm17ctt.g6.notification.dto.response.NotificationResponseDTO;
import ktpm17ctt.g6.notification.entity.Notification;
import ktpm17ctt.g6.notification.mapper.NotificationMapper;
import ktpm17ctt.g6.notification.repository.NotificationRepository;
import ktpm17ctt.g6.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

	private final NotificationRepository notificationRepository;
	private final NotificationMapper notificationMapper;
	
	@Override
	public NotificationResponseDTO createNotification(NotificationRequestDTO requestDTO) {
        Notification notification = notificationMapper.toEntity(requestDTO);
        notification = notificationRepository.save(notification);
        return notificationMapper.toResponseDTO(notification);
	}
}
