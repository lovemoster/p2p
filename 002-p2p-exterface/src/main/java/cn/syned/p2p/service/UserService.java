package cn.syned.p2p.service;

import cn.syned.p2p.entity.User;

/**
 * 用户业务接口
 */
public interface UserService {
    /**
     * 查询平台用户数
     *
     * @return 平台用户数
     */
    Long queryAllUserCount();

    /**
     * 判断手机号是否注册
     *
     * @param phone 手机号
     * @return 布尔值 true 代表该手机号已注册， false 代表改手机号未被注册
     */
    Boolean checkPhone(String phone);

    /**
     * 用户注册方法
     *
     * @param user 用户实体
     * @return 注册状态 true 代表注册成功， false 代表注册失败
     */
    Boolean register(User user);

    /**
     * 用户实名方法
     *
     * @param user 用户实体
     * @return 注册状态 true 代表注册成功， false 代表注册失败
     */
    Boolean realName(User user);

    /**
     * 用户登录方法
     *
     * @param user 要验证的用户
     * @return 登录的用户对象
     */
    User login(User user);
}
