package ezinsurance.dm;

import ezinsurance.vo.CustomerVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Repository
@Mapper
public interface TBMSACM010DM {
    CustomerVO selectOneTBMSACM010a(String custNo);
}
