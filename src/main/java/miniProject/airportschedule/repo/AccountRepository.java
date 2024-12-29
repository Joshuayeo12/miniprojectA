package miniProject.airportschedule.repo;


import miniProject.airportschedule.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class AccountRepository {

    @Autowired
    @Qualifier("redisStringTemplate") // RedisConfig template
    private RedisTemplate<String, String> template;

    private static final String REDIS_KEY = "Accounts";

    public void save(String username, String password) {
        HashOperations<String, String, String> hashOps = template.opsForHash();
        hashOps.put(REDIS_KEY, username, password);
    }

    public String findPasswordByUsername(String username) {
        HashOperations<String, String, String> hashOps = template.opsForHash();
        return hashOps.get(REDIS_KEY, username);
    }
}