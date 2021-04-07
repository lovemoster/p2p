package cn.syned.p2p.service;

import cn.syned.p2p.entity.RechargeRecord;
import cn.syned.p2p.mapper.RechargeRecordMapper;
import com.alipay.easysdk.factory.Factory;
import com.alipay.easysdk.kernel.Config;
import com.alipay.easysdk.payment.common.models.AlipayTradeQueryResponse;
import com.alipay.easysdk.payment.page.models.AlipayTradePagePayResponse;
import org.apache.commons.lang.time.DateUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@DubboService(interfaceClass = RechargeService.class, version = "1.0.0", timeout = 20000)
public class RechargeServiceImpl implements RechargeService {

    private Config config;
    private RechargeRecordMapper rechargeRecordMapper;

    @DubboReference(interfaceClass = FinaceAccountService.class, version = "1.0.0", timeout = 20000)
    public FinaceAccountService finaceAccountService;

    @Autowired
    public void setRechargeRecordMapper(RechargeRecordMapper rechargeRecordMapper) {
        this.rechargeRecordMapper = rechargeRecordMapper;
    }

    @Autowired
    public void setConfig(Config config) {
        this.config = config;
    }

    /**
     * 通过支付方式调用不同第三方充值接口
     *
     * @param paymentMethod  充值方式
     * @param rechargeRecord 订单对象
     * @return 三方页面
     */
    @Override
    @Transactional
    public String recharge(String paymentMethod, RechargeRecord rechargeRecord) {
        int count = rechargeRecordMapper.insertSelective(rechargeRecord);
        //如果创建订单失败则回滚事务
        if (count != 1) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        if ("alipay".equals(paymentMethod)) {
            return aliPayMethod(rechargeRecord);
        }
        return "";
    }

    /**
     * 支付宝支付
     *
     * @param rechargeRecord
     * @return
     */
    public String aliPayMethod(RechargeRecord rechargeRecord) {
        Factory.setOptions(config);
        AlipayTradePagePayResponse response = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        try {
            response = Factory.Payment.Page()
                    .optional("time_expire", sdf.format(DateUtils.addMinutes(new Date(), 1)))
                    .pay(
                            "测试",
                            rechargeRecord.getRechargeNo(),
                            rechargeRecord.getRechargeMoney().toString(),
                            "http://127.0.0.1/loan/myRecharge");
            if (response != null) {
                return response.getBody();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 微信支付
     *
     * @return
     */
//    public String weChatPayMethod(RechargeRecord rechargeRecord) {
//        WechatPayHttpClientBuilder builder = WechatPayHttpClientBuilder.create()
//                .withMerchant()
//                .withWechatpay();
//        // 通过WechatPayHttpClientBuilder构造的HttpClient，会自动的处理签名和验签
//        HttpClient httpClient = builder.build();
//        //设置请求地址
//        HttpPost post = new HttpPost("https://api.mch.weixin.qq.com/v3/pay/transactions/native");
//        //设置参数
//        HashMap<String, Object> params = new HashMap<>();
//        HttpResponse response = null;
//        try {
//            //转换为请求实体
//            StringEntity entity = new StringEntity(JSON.toJSONString(params));
//            //设置请求实体
//            post.setEntity(entity);
//            post.setHeader("User-Agent","");
//            response = httpClient.execute(post);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        assert response != null;
//        String code_url = "";
//        try {
//            String entity = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
//            code_url = JSON.parseObject(entity).getString("code_url");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return code_url;
//    }

    /**
     * 根据用户ID查询用户充值记录
     *
     * @param uid 用户ID
     * @return 用户充值列表
     */
    @Override
    public List<RechargeRecord> queryRechargeRecordByUID(Integer uid) {
        return rechargeRecordMapper.selectRechargeRecordByUID(uid);
    }

    /**
     * 查询订单的支付状态
     */
    @Override
    public void queryPaymentStatus() {
        //查询所有支付状态为未支付的订单
        List<RechargeRecord> rechargeRecordList = rechargeRecordMapper.selectRechargeRecordByStatus();
        if (rechargeRecordList.size() == 0) {
            return;
        }
        //遍历所有订单，查询是否交易成功
        for (RechargeRecord rechargeRecord : rechargeRecordList) {
            Factory.setOptions(config);
            try {
                AlipayTradeQueryResponse response = Factory.Payment.Common()
                        .optional("trade_no", null)
                        .query(rechargeRecord.getRechargeNo());
                if ("TRADE_SUCCESS".equals(response.tradeStatus)) {
                    rechargeRecord.setRechargeStatus("1");
                    int count = rechargeRecordMapper.updateByPrimaryKeySelective(rechargeRecord);
                    if (count != 1) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return;
                    }
                    count = finaceAccountService.updateAvailableMoneyByUID(rechargeRecord.getUid(), rechargeRecord.getRechargeMoney());
                    if (count != 1) {
                        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                        return;
                    }
                }
                if ("TRADE_CLOSED".equals(response.tradeStatus)) {
                    rechargeRecord.setRechargeStatus("2");
                    rechargeRecordMapper.updateByPrimaryKeySelective(rechargeRecord);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
