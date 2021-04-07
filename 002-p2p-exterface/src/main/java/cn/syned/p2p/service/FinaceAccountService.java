package cn.syned.p2p.service;

import cn.syned.p2p.entity.FinaceAccount;

/**
 *  投资账户接口
 */
public interface FinaceAccountService {
    /**
     * 根据用户ID查询账户信息
     *
     * @param id 用户ID
     * @return 账户对象
     */
    FinaceAccount queryFinaceAccountByUserId(Integer id);

    int updateAvailableMoneyByUID(Integer uid, Double rechargeMoney);
}
