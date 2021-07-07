package ezinsurance.dm;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import ezinsurance.vo.PrpsRelsVO;

@Repository
@Mapper
public interface TBMSANB002DM {

    PrpsRelsVO selectOneTBMSANB002a(String prpsNo, String custContRelcd, String custNo, Integer relpSeq);

    List<PrpsRelsVO> selectListTBMSANB002a(String ppsdsnNo);

    int updateTBMSANB002a(ezinsurance.vo.PrpsRelsVO planRelsInfo);

    int insertTBMSANB002a(ezinsurance.vo.PrpsRelsVO planRelsInfo);
}
