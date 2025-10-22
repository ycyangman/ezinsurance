package ezinsurance.dm;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import ezinsurance.vo.PlanInsVO;

@Repository
@Mapper
public interface TBMSAPL003DM {

    PlanInsVO selectOneTBMSAPL003a(String dsnInsNo);

    List<PlanInsVO> selectListTBMSAPL003a(String ppsdsnNo);

    int updateTBMSAPL003a(ezinsurance.vo.PlanInsVO planRelsInfo);

    int insertTBMSAPL003a(ezinsurance.vo.PlanInsVO planRelsInfo);
}
