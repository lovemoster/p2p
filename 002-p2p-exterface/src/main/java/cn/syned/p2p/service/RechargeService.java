package cn.syned.p2p.service;

import cn.syned.p2p.entity.RechargeRecord;

import java.util.List;

/**
 * 充值业务接口
 */
public interface RechargeService {
    List<RechargeRecord> queryRechargeRecordByUID(Integer uid);

    /**
     * 充值业务接口
     *
     * @param paymentMethod  充值方式
     * @param rechargeRecord 订单对象
     * @return 充值页面
     */
    String recharge(String paymentMethod, RechargeRecord rechargeRecord);

    void queryPaymentStatus();
}
