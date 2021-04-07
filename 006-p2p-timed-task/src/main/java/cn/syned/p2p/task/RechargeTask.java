package cn.syned.p2p.task;

import cn.syned.p2p.service.RechargeService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class RechargeTask {
    @DubboReference(interfaceClass = RechargeService.class, version = "1.0.0", timeout = 20000)
    RechargeService rechargeService;

    /**
     * 设置定时任务，每五秒扫描一次产品表，添加满标产品到收益记录表
     */
    @Scheduled(cron = "0/30 * * * * ?")
    public void rechargeTask() {
        rechargeService.queryPaymentStatus();
    }
}
