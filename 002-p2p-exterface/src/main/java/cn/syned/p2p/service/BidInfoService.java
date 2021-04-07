package cn.syned.p2p.service;

import cn.syned.p2p.entity.BidInfo;
import cn.syned.p2p.exception.BidException;

import java.util.List;
import java.util.Map;

/**
 * 投资业务接口
 */
public interface BidInfoService {
    /**
     * 查询累计成交额
     *
     * @return 交易额
     */
    Double queryHistoryTurnover();

    /**
     * 根据产品ID查询交易记录
     *
     * @param loanId 产品ID
     * @return 交易记录列表
     */
    List<BidInfo> queryBidInfoByLoanId(Integer loanId);

    /**
     * 投资业务
     *
     * @param parasMap 投资业务参数
     */
    void invest(Map<String, Object> parasMap) throws BidException;
}
