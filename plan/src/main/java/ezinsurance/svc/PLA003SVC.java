package ezinsurance.svc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import ezinsurance.PlanApplication;
import ezinsurance.io.PLA00200Out;
import ezinsurance.support.ExecuteService;
import ezinsurance.support.external.PublishEvent;
import ezinsurance.vo.PlanInfoVO;
import ezinsurance.dm.TBMSAPL001DM;


@Service("PLA003SVC")
public class PLA003SVC extends ExecuteService<PLA00200Out>{
    
    @Autowired
    private TBMSAPL001DM  tbmsapl001DM;

    @Autowired
    private PublishEvent publishEvent;


    public PLA00200Out execute(HashMap<?, ?> param) {

        System.out.println("\n##### PLA002SVC param : " + param + "\n");

        PLA00200Out out = new PLA00200Out();

        //서비스기능
        String svcFn = (String)param.get("svcFn");

        String ppsdsnNo = (String)param.get("ppsdsnNo");
        

        PlanInfoVO  planInfo = tbmsapl001DM.selectOneTBMSAPL001a(ppsdsnNo);

        //상설발행
        if("prodDesdIsue".equalsIgnoreCase(svcFn)) {

            Map<String, String> dmParam = new HashMap<String, String>();

            dmParam.put("ppsdsnNo", ppsdsnNo);
            dmParam.put("progSt", "상품설명서발행요청중");

            int upCnt = tbmsapl001DM.updateOneTBMSAPL001a(dmParam);
            System.out.println("upCnt:" + upCnt);

            // 상품MSA에 메시지 전송

            if (planInfo != null) {
                planInfo.setEventType("ProductDesdRequested");
            }
            
            publishEvent.publish(planInfo);

        }

        if (planInfo != null) {
            BeanUtils.copyProperties(planInfo, out);
        }


        return out;
    }
}

