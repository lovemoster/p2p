package cn.syned.p2p.service;

import cn.syned.p2p.entity.LoanInfo;
import cn.syned.p2p.entity.Page;
import cn.syned.p2p.mapper.LoanInfoMapper;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 产品信息业务实现类
 *
 * @version 1.0.0
 */
@Component
@DubboService(interfaceClass = LoanInfoService.class, version = "1.0.0", timeout = 20000)
public class LoanInfoServiceImpl implements LoanInfoService {

    private LoanInfoMapper loanInfoMapper;

    private RedisTemplate redisTemplate;

    @Autowired
    public void setLoanInfoMapper(LoanInfoMapper loanInfoMapper) {
        this.loanInfoMapper = loanInfoMapper;
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 查询历史年化收益率
     *
     * @return 历史年化收益率
     */
    @Override
    public Double queryHistoryRate() {
        //查询缓存
        Double historyRate = (Double) redisTemplate.opsForValue().get("historyRate");
        //判断缓存中是否有数据
        if (historyRate == null) {
            //进行线程同步
            synchronized (this) {
                historyRate = (Double) redisTemplate.opsForValue().get("historyRate");
                if (historyRate == null) {
                    //从数据库查询
                    historyRate = loanInfoMapper.selectHistoryRate();
                    //设置缓存
                    redisTemplate.opsForValue().set("historyRate", historyRate, 24, TimeUnit.HOURS);
                }
            }
        }
        return historyRate;
    }

    /**
     * 通过产品类型查询产品信息列表
     *
     * @param queryParameter 查询参数
     * @return 产品信息列表
     */
    @Override
    public List<LoanInfo> queryLoanInfoByType(HashMap<String, Object> queryParameter) {
        return loanInfoMapper.selectLoanInfoByType(queryParameter);
    }

    /**
     * 通过类型和分页参数查询产品信息
     *
     * @param ptype 产品类型
     * @param page  分页参数
     * @return 产品列表
     */
    @Override
    public List<LoanInfo> queryLoanInfoByTypeAndPage(Integer ptype, Page page) {
        //判断分页参数是否正确
        if (page == null) {
            page = new Page(1, 9);
        }
        if (page.getCunPage() == null) {
            page.setCunPage(1);
        }
        if (page.getPageContent() == null) {
            page.setPageContent(9);
        }
        HashMap<String, Object> queryParameters = new HashMap<>();
        queryParameters.put("ptype", ptype);
        queryParameters.put("startWith", (page.getCunPage() - 1) * page.getPageContent());
        queryParameters.put("endWith", page.getPageContent());
        return loanInfoMapper.queryLoanInfoByTypeAndPage(queryParameters);
    }

    /**
     * 根据产品信息查询分页总记录数
     *
     * @param ptype 产品类型
     * @return 总记录条数
     */
    @Override
    public Long queryLoanInfoByTypeAndPageCount(Integer ptype) {
        return loanInfoMapper.selectLoanInfoByTypeAndPageCount(ptype);
    }

    /**
     * 根据产品ID查询产品信息
     *
     * @param loanId 产品ID
     * @return 产品对象
     */
    @Override
    public LoanInfo queryLoanInfoById(Integer loanId) {
        return loanInfoMapper.selectByPrimaryKey(loanId);
    }
}
