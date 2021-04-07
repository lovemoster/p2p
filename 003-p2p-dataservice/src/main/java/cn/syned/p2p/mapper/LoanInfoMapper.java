package cn.syned.p2p.mapper;

import cn.syned.p2p.entity.LoanInfo;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public interface LoanInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LoanInfo record);

    int insertSelective(LoanInfo record);

    LoanInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LoanInfo record);

    int updateByPrimaryKey(LoanInfo record);

    /**
     * 查询历史年化收益率
     *
     * @return 历史年化收益率
     */
    Double selectHistoryRate();

    /**
     * 通过产品类型查询产品信息列表
     *
     * @param queryParameter 查询参数
     * @return 产品信息列表
     */
    List<LoanInfo> selectLoanInfoByType(HashMap<String, Object> queryParameter);

    /**
     * 通过类型和分页参数查询产品信息
     *
     * @param queryParameters 查询参数
     * @return 产品列表
     */
    List<LoanInfo> queryLoanInfoByTypeAndPage(HashMap<String, Object> queryParameters);

    /**
     * 根据产品类型编号查询总记录数
     *
     * @param ptype 产品类型编号
     * @return 此产品编号的记录数
     */
    Long selectLoanInfoByTypeAndPageCount(Integer ptype);

    /**
     * 投资业务：更新产品剩余可投金额
     *
     * @param parasMap 待更新的参数
     * @return 影响记录条数
     */
    int updateLoanInfoByBidMoneyAndLoanId(Map<String, Object> parasMap);

    /**
     * 查询所有满标的产品
     *
     * @return 满标的产品列表
     */
    List<LoanInfo> selectAllWhereFullMark();
}