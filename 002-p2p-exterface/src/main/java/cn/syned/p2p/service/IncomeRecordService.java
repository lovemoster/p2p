package cn.syned.p2p.service;

/**
 * 收益计划业务
 */
public interface IncomeRecordService {
    /**
     * 设置定时任务，每五秒扫描一次产品表，添加满标产品到收益记录表
     */
    void generatePlan();

    /**
     * 设置定时任务，每五秒扫描一次交易计划表，到期产品返还用户收益
     */
    void reappearance();
}
