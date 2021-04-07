package cn.syned.p2p.service;

import cn.syned.p2p.entity.LoanInfo;
import cn.syned.p2p.entity.Page;

import java.util.HashMap;
import java.util.List;

/**
 * 产品信息业务接口
 */
public interface LoanInfoService {
    /**
     * 查询历史年化收益率
     *
     * @return 历史年化收益率
     */
    Double queryHistoryRate();

    /**
     * 通过产品类型查询产品信息列表
     *
     * @param queryParameter 查询参数
     * @return 产品信息列表
     */
    List<LoanInfo> queryLoanInfoByType(HashMap<String, Object> queryParameter);

    /**
     * 通过类型和分页参数查询产品信息
     *
     * @param ptype 产品类型
     * @param page  分页参数
     * @return 产品列表
     */
    List<LoanInfo> queryLoanInfoByTypeAndPage(Integer ptype, Page page);

    /**
     * 根据产品信息查询分页总记录数
     *
     * @param ptype 产品类型
     * @return 总记录条数
     */
    Long queryLoanInfoByTypeAndPageCount(Integer ptype);

    /**
     * 根据产品ID查询产品信息
     *
     * @param loanId 产品ID
     * @return 产品对象
     */
    LoanInfo queryLoanInfoById(Integer loanId);
}
