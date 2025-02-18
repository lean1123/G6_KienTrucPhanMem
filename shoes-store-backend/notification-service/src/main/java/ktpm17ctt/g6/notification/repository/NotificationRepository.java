package ktpm17ctt.g6.notification.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import ktpm17ctt.g6.notification.entity.Notification;

public interface NotificationRepository extends MongoRepository<Notification, String>{

}
