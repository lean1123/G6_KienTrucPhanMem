package ktpm17ctt.g6.notification.controller;


import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ktpm17ctt.g6.notification.dto.request.NotificationRequestDTO;
import ktpm17ctt.g6.notification.dto.response.NotificationResponseDTO;
import ktpm17ctt.g6.notification.service.NotificationService;


@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationResponseDTO> createNotification(@RequestBody NotificationRequestDTO requestDTO) {
        NotificationResponseDTO responseDTO = notificationService.createNotification(requestDTO);
        return ResponseEntity.ok(responseDTO);
    }
}
