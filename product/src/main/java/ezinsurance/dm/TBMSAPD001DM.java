package ezinsurance.dm;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import ezinsurance.vo.ProductVO;

@Repository
@Mapper
public interface TBMSAPD001DM {

    ProductVO selectOneTBMSAPD001a(String prdcd);
    
    //int insertTBMSAPL001a(java.util.Map<String, String> map);
}
