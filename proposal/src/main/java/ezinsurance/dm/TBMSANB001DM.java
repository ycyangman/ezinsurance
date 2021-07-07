package ezinsurance.dm;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import ezinsurance.vo.PrpsInfoVO;

@Repository
@Mapper
public interface TBMSANB001DM {

    PrpsInfoVO selectOneTBMSANB001a(String ppsdsnNo);
    List<PrpsInfoVO> selectListTBMSANB001a(String custNo);

    int updateOneTBMSANB001a(java.util.Map<String, String> map);
}
