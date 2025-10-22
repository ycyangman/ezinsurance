package ezinsurance.io;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ezinsurance.support.DefaultDTO;
import ezinsurance.vo.PlanInfoVO;
import ezinsurance.vo.PlanInsVO;
import ezinsurance.vo.PlanRelsVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PLA00200In extends DefaultDTO{
    
    PlanInfoVO planInfo;

    List<PlanRelsVO> planRelsList;  //설계관계자

    List<PlanInsVO> planInsList;  //설계보험

}

