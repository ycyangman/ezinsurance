package ezinsurance.dm;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import ezinsurance.vo.PlanInsVO;

@Repository
@Mapper
public interface TBMSAPL003DM {

    PlanInsVO selectOneTBMSAPL003(String dsnInsNo);

    List<PlanInsVO> selectListTBMSAPL003(String ppsdsnNo);

    int updateTBMSAPL003(ezinsurance.vo.PlanInsVO planRelsInfo);

    int insertTBMSAPL003(ezinsurance.vo.PlanInsVO planRelsInfo);
}
