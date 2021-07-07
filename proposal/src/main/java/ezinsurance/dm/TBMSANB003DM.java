package ezinsurance.dm;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import ezinsurance.vo.PrpsInsVO;

@Repository
@Mapper
public interface TBMSANB003DM {

    PrpsInsVO selectOneTBMSAPL003(String prpsInsNo);

    List<PrpsInsVO> selectListTBMSAPL003(String prpsNo);

    int updateTBMSAPL003(ezinsurance.vo.PrpsInsVO prpsRelsInfo);

    int insertTBMSAPL003(ezinsurance.vo.PrpsInsVO prpsRelsInfo);
}
