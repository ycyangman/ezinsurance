package ezinsurance.dm;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import ezinsurance.vo.PlanRelsVO;

@Repository
@Mapper
public interface TBMSAPL002DM {

    PlanRelsVO selectOneTBMSAPL002a(String ppsdsnNo, String custContRelcd, String custNo, Integer relpSeq);

    List<PlanRelsVO> selectListTBMSAPL002a(String ppsdsnNo);

    int updateTBMSAPL002a(ezinsurance.vo.PlanRelsVO planRelsInfo);

    int insertTBMSAPL002a(ezinsurance.vo.PlanRelsVO planRelsInfo);
}
