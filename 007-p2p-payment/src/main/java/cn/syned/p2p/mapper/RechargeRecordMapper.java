package cn.syned.p2p.mapper;

import cn.syned.p2p.entity.RechargeRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RechargeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RechargeRecord record);

    int insertSelective(RechargeRecord record);

    RechargeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RechargeRecord record);

    int updateByPrimaryKey(RechargeRecord record);

    /**
     * 根据用户ID查询用户充值记录
     *
     * @param uid 用户ID
     * @return 充值记录列表
     */
    List<RechargeRecord> selectRechargeRecordByUID(Integer uid);

    /**
     * 查询所有状态为未支付的订单
     *
     * @return 充值记录列表
     */
    List<RechargeRecord> selectRechargeRecordByStatus();
}