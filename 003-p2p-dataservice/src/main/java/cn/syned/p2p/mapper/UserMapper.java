package cn.syned.p2p.mapper;

import cn.syned.p2p.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    Long selectUserCount();

    /**
     * 通过手机号查询用户
     * @param phone 手机号码
     * @return 用户对象
     */
    User selectUserByPhone(String phone);

    /**
     * 通过手机号实名用户
     * @param user 手机号码
     * @return 用户对象
     */
    int realName(User user);

    User login(User user);
}