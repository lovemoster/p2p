package cn.syned.p2p.service;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@DubboService(interfaceClass = RedisService.class, version = "1.0.0", timeout = 20000)
public class RedisServiceImpl implements RedisService {

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void pushCode(String phone, String verificationCode) {
        redisTemplate.opsForValue().set(phone, verificationCode, 5, TimeUnit.MINUTES);
    }

    //验证码:根据电话号码获取验证码
    @Override
    public String popCode(String phone) {
        return redisTemplate.opsForValue().get(phone) + "";
    }
}
