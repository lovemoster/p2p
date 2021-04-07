package cn.syned.p2p.service;

import cn.syned.p2p.entity.BidInfo;
import cn.syned.p2p.entity.LoanInfo;
import cn.syned.p2p.enums.BidEnum;
import cn.syned.p2p.exception.BidException;
import cn.syned.p2p.mapper.BidInfoMapper;
import cn.syned.p2p.mapper.FinaceAccountMapper;
import cn.syned.p2p.mapper.LoanInfoMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 投资业务实现类
 */
@Component
@DubboService(interfaceClass = BidInfoService.class, version = "1.0.0", timeout = 20000)
public class BidInfoServiceImpl implements BidInfoService {

    private BidInfoMapper bidInfoMapper;
    private RedisTemplate redisTemplate;
    private FinaceAccountMapper finaceAccountMapper;
    private LoanInfoMapper loanInfoMapper;


    @Autowired
    public void setBidInfoMapper(BidInfoMapper bidInfoMapper){
        this.bidInfoMapper = bidInfoMapper;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Autowired
    public void setFinaceAccountMapper(FinaceAccountMapper finaceAccountMapper) {
        this.finaceAccountMapper = finaceAccountMapper;
    }

    @Autowired
    public void setLoanInfoMapper(LoanInfoMapper loanInfoMapper) {
        this.loanInfoMapper = loanInfoMapper;
    }

    /**
     * 查询累计成交额
     *
     * @return 交易额
     */
    @Override
    public Double queryHistoryTurnover() {
        //从缓存中查询是否有数据
        Double historyTurnover = (Double) redisTemplate.opsForValue().get("historyTurnover");
        //如果缓存中没有数据
        if (historyTurnover == null) {
            //进行线程同步
            synchronized (this) {
                historyTurnover = (Double) redisTemplate.opsForValue().get("historyTurnover");
                if (historyTurnover == null) {
                    //从数据库查询
                    historyTurnover = bidInfoMapper.selectHistoryTurnover();
                    //设置缓存
                    redisTemplate.opsForValue().set("historyTurnover", historyTurnover, 20, TimeUnit.HOURS);
                }
            }
        }
        return historyTurnover;
    }

    /**
     * 根据产品ID查询交易记录
     *
     * @param loanId 产品ID
     * @return 交易记录列表
     */
    @Override
    public List<BidInfo> queryBidInfoByLoanId(Integer loanId) {
        return bidInfoMapper.selectBidInfoByLoanId(loanId);
    }

    /**
     * 投资业务实现
     *
     * @param parasMap 投资业务参数
     */
    @Override
    @Transactional
    public void invest(Map<String, Object> parasMap) throws BidException {
        //判断账户剩余金额是否满足此次投资
        int count = finaceAccountMapper.updateAvailableMoneyByBidMoney(parasMap);
        if (count != 1) {
            //如果更新失败直接回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BidException(BidEnum.BID_MONEY_INSUFFICIENT);
        }
        //查询数据库获取修改版本号
        LoanInfo loInfo = loanInfoMapper.selectByPrimaryKey(Integer.parseInt(parasMap.get("loanId") + ""));
        //添加版本号信息
        parasMap.put("version", loInfo.getVersion());
        //判断产品剩余金额是否满足此次投资，如果不满足则回滚事务
        count = loanInfoMapper.updateLoanInfoByBidMoneyAndLoanId(parasMap);
        if (count != 1) {
            //如果更新失败直接回滚事务
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new BidException(BidEnum.BID_UPDATE_LOAN_INFO_MONEY_FAIL);
        }
        //如果产品剩余可投金额为0时，满标（1）
        LoanInfo loanInfo = loanInfoMapper.selectByPrimaryKey(Integer.parseInt(parasMap.get("loanId") + ""));
        if (loanInfo.getLeftProductMoney() == 0d && loanInfo.getProductStatus() == 0) {
            loanInfo.setProductStatus(1);
            loanInfo.setProductFullTime(new Date());
            count = loanInfoMapper.updateByPrimaryKeySelective(loanInfo);
            if (count != 1) {
                //如果更新失败直接回滚事务
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                throw new BidException(BidEnum.BID_UPDATE_FULL_MARK_FAIL);
            }
        }
        //插入投资记录
        BidInfo bidInfo = new BidInfo();
        bidInfo.setBidMoney(Double.parseDouble(parasMap.get("bidMoney") + ""));
        bidInfo.setBidStatus(1);
        bidInfo.setBidTime(new Date());
        bidInfo.setLoanId(Integer.parseInt(parasMap.get("loanId") + ""));
        bidInfo.setUid(Integer.parseInt(parasMap.get("uid") + ""));
        count = bidInfoMapper.insertSelective(bidInfo);
        if (count != 1) {
            throw new BidException(BidEnum.BID_UPDATE_FULL_MARK_FAIL);
        }
    }
}
