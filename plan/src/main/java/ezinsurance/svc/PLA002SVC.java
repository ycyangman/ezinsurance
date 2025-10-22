package ezinsurance.svc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ezinsurance.bm.PlanPrcsBM;
import ezinsurance.io.PLA00200In;
import ezinsurance.support.util.FwkUtils;
import ezinsurance.vo.ProductVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import ezinsurance.io.PLA00200Out;
import ezinsurance.support.ExecuteService;
import ezinsurance.vo.PlanInfoVO;
import ezinsurance.dm.TBMSAPL001DM;


@Service("PLA002SVC")
public class PLA002SVC extends ExecuteService<PLA00200Out>{
    
    @Autowired
    private TBMSAPL001DM  tbmsapl001DM;

    @Autowired
    private PlanPrcsBM planPrcsBM;

    public PLA00200Out execute(HashMap<?, ?> param) {

        System.out.println("\n##### PLA002SVC param : " + param + "\n");

        PLA00200Out out = new PLA00200Out();

        //서비스기능
        String svcFn = (String)param.get("svcFn");

        String ppsdsnNo = (String)param.get("ppsdsnNo");
        
        //설계저장
        if("savePlan".equalsIgnoreCase(svcFn)) {

            String jsonStr= FwkUtils.toJson(param);
            System.out.println("\n##### PLA002SVC FwkUtils.toJson(param) : " + jsonStr + "\n");

            PLA00200In planIn = FwkUtils.jsonToObject(jsonStr, PLA00200In.class);

            System.out.println("\n##### PLA002SVC planIn : " + planIn.toString() + "\n");

            ppsdsnNo = planPrcsBM.savePlan(planIn);

            out.setPpsdsnNo(ppsdsnNo);
        }

        //설계조회
        if("getPlan".equalsIgnoreCase(svcFn)) {

            PlanInfoVO planInfo = tbmsapl001DM.selectOneTBMSAPL001a(ppsdsnNo);

            if (planInfo != null) {
                BeanUtils.copyProperties(planInfo, out);
            }

            String custNo = (String)param.get("custNo");
            if(!StringUtils.isEmpty(custNo)) {

                List<PlanInfoVO>  planInfoList = tbmsapl001DM.selectListTBMSAPL001a(custNo);

                out.setPlanInfoList(planInfoList);
            }

        }


        return out;
    }
}

