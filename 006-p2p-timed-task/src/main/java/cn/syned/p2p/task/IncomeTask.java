package cn.syned.p2p.task;

import cn.syned.p2p.service.IncomeRecordService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class IncomeTask {
    @DubboReference(interfaceClass = IncomeRecordService.class, version = "1.0.0", timeout = 20000)
    IncomeRecordService incomeRecordService;

    /**
     * 设置定时任务，每五秒扫描一次产品表，添加满标产品到收益记录表
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void incomeTask() {
        incomeRecordService.generatePlan();
    }

    /**
     * 设置定时任务，每五秒扫描一次交易计划表，到期产品返还用户收益
     */
    @Scheduled(cron = "0/5 * * * * ?")
    public void reappearanceTask() {
        incomeRecordService.reappearance();
    }
}
