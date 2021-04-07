package cn.syned.p2p.service;

import cn.syned.p2p.entity.BidInfo;
import cn.syned.p2p.entity.IncomeRecord;
import cn.syned.p2p.entity.LoanInfo;
import cn.syned.p2p.mapper.BidInfoMapper;
import cn.syned.p2p.mapper.FinaceAccountMapper;
import cn.syned.p2p.mapper.IncomeRecordMapper;
import cn.syned.p2p.mapper.LoanInfoMapper;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 投资记录业务实现类
 */
@Component
@DubboService(interfaceClass = IncomeRecordService.class, version = "1.0.0", timeout = 20000)
public class IncomeRecordServiceImpl implements IncomeRecordService {

    private LoanInfoMapper loanInfoMapper;
    private IncomeRecordMapper incomeRecordMapper;
    private BidInfoMapper bidInfoMapper;
    private FinaceAccountMapper finaceAccountMapper;

    @Autowired
    public void setLoanInfoMapper(LoanInfoMapper loanInfoMapper) {
        this.loanInfoMapper = loanInfoMapper;
    }

    @Autowired
    public void setIncomeRecordMapper(IncomeRecordMapper incomeRecordMapper) {
        this.incomeRecordMapper = incomeRecordMapper;
    }

    @Autowired
    public void setBidInfoMapper(BidInfoMapper bidInfoMapper) {
        this.bidInfoMapper = bidInfoMapper;
    }

    @Autowired
    public void setFinaceAccountMapper(FinaceAccountMapper finaceAccountMapper) {
        this.finaceAccountMapper = finaceAccountMapper;
    }

    /**
     * 设置定时任务，每五秒扫描一次产品表，添加满标产品到收益记录表
     */
    @Override
    @Transactional
    public void generatePlan() {
        //查询所有满标的产品
        List<LoanInfo> loanInfoList = loanInfoMapper.selectAllWhereFullMark();
        //如果没有产品满标，则直接返回
        if (loanInfoList == null) {
            return;
        }
        //2、遍历产品 ，根据产品id 到投资表 查询该产品的投资记录  ==》List
        for (LoanInfo loanInfo : loanInfoList) {
            //根据产品id查询投资记录
            List<BidInfo> bidInfos = bidInfoMapper.selectBidInfosByLoanId(loanInfo.getId());
            //3、遍历投资记录 ，计算收益时间、收益金额-->收益表
            for (BidInfo bidInfo : bidInfos) {
                IncomeRecord incomeRecord = new IncomeRecord();
                incomeRecord.setBidId(bidInfo.getId());
                incomeRecord.setBidMoney(bidInfo.getBidMoney());
                Date date;
                double incomeMoney;
                if (loanInfo.getProductType() == 0) {
                    date = DateUtils.addDays(loanInfo.getProductFullTime(), loanInfo.getCycle());
                    incomeMoney = loanInfo.getRate() / 100 / 365 * loanInfo.getCycle() * bidInfo.getBidMoney();
                } else {
                    date = DateUtils.addMonths(loanInfo.getProductFullTime(), loanInfo.getCycle());
                    incomeMoney = loanInfo.getRate() / 100 / 365 * loanInfo.getCycle() * 30 * bidInfo.getBidMoney();

                }
                incomeRecord.setIncomeDate(date);
                incomeRecord.setIncomeMoney(incomeMoney);
                incomeRecord.setIncomeStatus(0);
                incomeRecord.setLoanId(loanInfo.getId());
                incomeRecord.setUid(bidInfo.getUid());
                incomeRecordMapper.insertSelective(incomeRecord);
            }
            //4、修改产品状态 为满标已生成收益计划（2）
            loanInfo.setProductStatus(2);
            loanInfoMapper.updateByPrimaryKeySelective(loanInfo);
        }
    }

    /**
     * 设置定时任务，每五秒扫描一次交易计划表，到期产品返还用户收益
     */
    @Override
    @Transactional
    public void reappearance() {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String formatDate = sdf.format(date);

        //查询投资记录表
        List<IncomeRecord> incomeRecordList = incomeRecordMapper.selectIncomeRecordByTimeAndStatus(formatDate);
        //根据投资记录返还用户本金
        for (IncomeRecord incomeRecord : incomeRecordList) {
            HashMap<String, Object> parameters = new HashMap<>();
            parameters.put("uid", incomeRecord.getUid());
            parameters.put("bidMoney", incomeRecord.getBidMoney());
            parameters.put("incomeMoney", incomeRecord.getIncomeMoney());
            //设置用户账户表
            int count = finaceAccountMapper.updateAvailableMoneyByIncomeRecord(parameters);
            incomeRecord.setIncomeStatus(1);
            //设置返还状态为已返还
            incomeRecordMapper.updateByPrimaryKeySelective(incomeRecord);
        }
    }
}
