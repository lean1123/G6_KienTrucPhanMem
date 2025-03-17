package ktpm17ctt.g6.gateway.controller;

import ktpm17ctt.g6.gateway.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/redis")
@RequiredArgsConstructor
public class RedisController {

    private final RedisService redisService;

    @PostMapping("/set")
    public String setData(@RequestParam String key, @RequestParam String value) {
        redisService.saveData(key, value, 600); // Lưu key với expiration 10 phút
        return "Saved!";
    }

    @GetMapping("/get")
    public String getData(@RequestParam String key) {
        return redisService.getData(key);
    }
}
