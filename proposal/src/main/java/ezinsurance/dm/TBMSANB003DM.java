package ezinsurance.dm;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import ezinsurance.vo.PrpsInsVO;

@Repository
@Mapper
public interface TBMSANB003DM {

    PrpsInsVO selectOneTBMSANB003a(String prpsInsNo);

    List<PrpsInsVO> selectListTBMSANB003a(String prpsNo);

    int updateOneTBMSANB003a(ezinsurance.vo.PrpsInsVO prpsInsInfo);

    int insertTBMSANB003a(ezinsurance.vo.PrpsInsVO prpsInsInfo);
}
