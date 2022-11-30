package com.example.redis;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
public class UserController {

    @Autowired
    RedisTemplate<String, User> redisTemplate;

    @Autowired
    ObjectMapper objectMapper;

    // ------------------------------X-----------------------------------------

    @PostMapping("/set_key")
    public void setKeyValue(@RequestParam("key") String key,
                            @RequestBody User user){

        redisTemplate.opsForValue().set(key, user);
    }

    @GetMapping("/get_value")
    public User getValueForKey(@RequestParam("key") String key){

        Object value = redisTemplate.opsForValue().get(key);

        return (User)value;
    }

    // -------------------------------X-----------------------------------------

    @PostMapping("/lpush")
    public void lpush(@RequestParam("key") String key, @RequestBody User user){

        Long listLength = redisTemplate.opsForList().leftPush(key, user);

    }

    @PostMapping("/rpush")
    public void rpush(@RequestParam("key") String key, @RequestBody User user){

        Long listLength = redisTemplate.opsForList().rightPush(key, user);

    }

    @GetMapping("/lpop")
    public User lpop(@RequestParam("key") String key){

        User user = (User)redisTemplate.opsForList().leftPop(key);

        return user;
    }

    @GetMapping("/rpop")
    public User rpop(@RequestParam("key") String key){

        User user = (User)redisTemplate.opsForList().rightPop(key);

        return user;
    }

    @GetMapping("/lrange")
    public List<User> lrange(@RequestParam("key") String key,
                             @RequestParam("start") int start,
                             @RequestParam("end") int end){

        return redisTemplate.opsForList().range(key, start, end);
//        return retrievedItems.stream().map(element -> (User) element).collect(Collectors.toList());
    }

    // --------------------------------------X---------------------------------------

    @PostMapping("/hmset")
    public void hmset(@RequestParam("key") String key, @RequestBody User user){
        Map map = objectMapper.convertValue(user, Map.class);
        redisTemplate.opsForHash().putAll(key, map);
    }

    @GetMapping("/hgetall")
    public User hgetAll(@RequestParam("key") String key){

        Map map = redisTemplate.opsForHash().entries(key);
        return objectMapper.convertValue(map, User.class);
    }

}