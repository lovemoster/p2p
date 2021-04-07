package cn.syned.p2p.mapper;

import cn.syned.p2p.entity.BidInfo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BidInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);

    /**
     * 查询累计成交额
     *
     * @return 交易额
     */
    Double selectHistoryTurnover();

    /**
     * 根据产品ID查询交易记录
     *
     * @param loanId 产品ID
     * @return 交易记录列表
     */
    List<BidInfo> selectBidInfoByLoanId(Integer loanId);

    /**
     * 定时任务：根据产品ID查询所有交易记录
     *
     * @param id 产品ID
     * @return 交易记录列表
     */
    List<BidInfo> selectBidInfosByLoanId(Integer id);
}