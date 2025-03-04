package ktpm17ctt.g6.gateway.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final StringRedisTemplate stringRedisTemplate;

    // Lưu dữ liệu vào Redis
    public void saveData(String key, String value, long expirationInSeconds) {
        stringRedisTemplate.opsForValue().set(key, value, Duration.ofSeconds(expirationInSeconds));
    }

    // Lấy dữ liệu từ Redis
    public String getData(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    // Xóa dữ liệu trong Redis
    public void deleteData(String key) {
        stringRedisTemplate.delete(key);
    }
}
