package cn.syned.p2p.mapper;

import cn.syned.p2p.entity.IncomeRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IncomeRecord record);

    int insertSelective(IncomeRecord record);

    IncomeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncomeRecord record);

    int updateByPrimaryKey(IncomeRecord record);

    List<IncomeRecord> selectIncomeRecordByTimeAndStatus(String formatDate);
}