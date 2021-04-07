package cn.syned.p2p.service;

public interface RedisService {
    void pushCode(String phone, String verificationCode);

    String popCode(String phone);
}
