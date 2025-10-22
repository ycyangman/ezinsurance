package ezinsurance.dm;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import ezinsurance.vo.PlanInfoVO;

@Repository
@Mapper
public interface TBMSAPL001DM {

    PlanInfoVO selectOneTBMSAPL001a(String ppsdsnNo);
    List<PlanInfoVO> selectListTBMSAPL001a(String custNo);

    int updateOneTBMSAPL001a(java.util.Map<String, String> map);

    int insertTBMSAPL001a(PlanInfoVO planInfo);

}
