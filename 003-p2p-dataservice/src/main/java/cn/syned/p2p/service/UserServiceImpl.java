package cn.syned.p2p.service;

import cn.syned.p2p.entity.FinaceAccount;
import cn.syned.p2p.entity.User;
import cn.syned.p2p.mapper.FinaceAccountMapper;
import cn.syned.p2p.mapper.UserMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


/**
 * 用户业务接口实现类
 *
 * @version 1.0.0
 */
@Component
@DubboService(interfaceClass = UserService.class, version = "1.0.0", timeout = 20000)
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;
    private FinaceAccountMapper finaceAccountMapper;
    private RedisTemplate redisTemplate;

    @Autowired
    public void setUserMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    @Autowired
    public void setFinaceAccountMapper(FinaceAccountMapper finaceAccountMapper) {
        this.finaceAccountMapper = finaceAccountMapper;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 查询平台用户数
     *
     * @return 平台用户数
     */
    @Override
    public Long queryAllUserCount() {
        //查询缓存
        Long userCount = (Long) redisTemplate.opsForValue().get("userCount");
        //判断缓存中是否有数据
        if (userCount == null) {
            //进行线程同步
            synchronized (this) {
                userCount = (Long) redisTemplate.opsForValue().get("userCount");
                if (userCount == null) {
                    //从数据库查询
                    userCount = userMapper.selectUserCount();
                    //设置缓存
                    redisTemplate.opsForValue().set("userCount", userCount, 24, TimeUnit.HOURS);
                }
            }
        }
        return userCount;
    }

    /**
     * 判断手机号是否注册
     *
     * @param phone 手机号
     * @return 布尔值 true 代表该手机号已注册， false 代表改手机号未被注册
     */
    @Override
    public Boolean checkPhone(String phone) {
        User user = userMapper.selectUserByPhone(phone);
        return user != null;
    }

    @Override
    @Transactional
    public Boolean register(User user) {
        //校验用户是否存在
        User resUser = userMapper.selectUserByPhone(user.getPhone());
        if (resUser != null) {
            return false;
        }
        //如果用户不存在执行注册流程
        user.setAddTime(new Date());
        int count = userMapper.insertSelective(user);
        resUser = userMapper.selectUserByPhone(user.getPhone());
        if (count == 1) {
            FinaceAccount finaceAccount = new FinaceAccount();
            finaceAccount.setUid(resUser.getId());
            finaceAccount.setAvailableMoney(888d);
            finaceAccountMapper.insertSelective(finaceAccount);
            return true;
        }
        return false;
    }

    /**
     * 用户实名方法
     *
     * @param user 用户实体
     * @return 注册状态 true 代表注册成功， false 代表注册失败
     */
    @Override
    @Transactional
    public Boolean realName(User user) {
        int count = userMapper.realName(user);
        return count == 1;
    }

    /**
     * 用户登录方法
     *
     * @param user 要验证的用户
     * @return 登录的用户对象
     */
    @Override
    public User login(User user) {
        //查询用户信息
        User resUser = userMapper.login(user);
        //如果查询到用户信息
        if (resUser != null) {
            //设置登录时间
            resUser.setLastLoginTime(new Date());
            ExecutorService executorService = Executors.newFixedThreadPool(6);
            executorService.submit(() -> {
                userMapper.updateByPrimaryKeySelective(resUser);
            });
        }
        return resUser;
    }
}
