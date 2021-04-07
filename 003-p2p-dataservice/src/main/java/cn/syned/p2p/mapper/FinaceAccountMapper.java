package cn.syned.p2p.mapper;

import cn.syned.p2p.entity.FinaceAccount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public interface FinaceAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FinaceAccount record);

    int insertSelective(FinaceAccount record);

    FinaceAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FinaceAccount record);

    int updateByPrimaryKey(FinaceAccount record);

    FinaceAccount selectFinaceAccountByUserId(Integer id);

    /**
     * 投资业务：根据投资金额修改账户可用金额
     *
     * @param parasMap 查询参数
     * @return 影响的记录条数
     */
    int updateAvailableMoneyByBidMoney(Map<String, Object> parasMap);

    /**
     * 通过收益表来返还用户本金和收益
     *
     * @param parameters 更新参数
     * @return 影响的记录条数
     */
    int updateAvailableMoneyByIncomeRecord(HashMap<String, Object> parameters);

    int updateAvailableMoneyByUID(@Param("uid") Integer uid, @Param("rechargeMoney") Double rechargeMoney);
}