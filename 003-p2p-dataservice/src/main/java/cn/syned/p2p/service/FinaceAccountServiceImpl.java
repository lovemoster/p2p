package cn.syned.p2p.service;

import cn.syned.p2p.entity.FinaceAccount;
import cn.syned.p2p.mapper.FinaceAccountMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 投资账户业务实现
 */
@Component
@DubboService(interfaceClass = FinaceAccountService.class, version = "1.0.0", timeout = 20000)
public class FinaceAccountServiceImpl implements FinaceAccountService {

    private FinaceAccountMapper finaceAccountMapper;

    @Autowired
    public void setFinaceAccountMapper(FinaceAccountMapper finaceAccountMapper) {
        this.finaceAccountMapper = finaceAccountMapper;
    }

    /**
     * 根据用户ID查询账户信息
     *
     * @param id 用户ID
     * @return 账户对象
     */
    @Override
    public FinaceAccount queryFinaceAccountByUserId(Integer id) {
        return finaceAccountMapper.selectFinaceAccountByUserId(id);
    }

    @Override
    public int updateAvailableMoneyByUID(Integer uid, Double rechargeMoney) {
        return finaceAccountMapper.updateAvailableMoneyByUID(uid, rechargeMoney);
    }
}
