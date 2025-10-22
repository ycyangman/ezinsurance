package ezinsurance.svc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import ezinsurance.io.NBA00100Out;
import ezinsurance.support.ExecuteService;
import ezinsurance.vo.PrpsInfoVO;
import ezinsurance.dm.TBMSANB001DM;
import ezinsurance.support.external.PublishEvent;

@Service("NBA002SVC")
public class NBA002SVC extends ExecuteService<NBA00100Out>{
    
    @Autowired
    private TBMSANB001DM  tbmsapl001DM;

    @Autowired
    private PublishEvent publishEvent;


    public NBA00100Out execute(HashMap<?, ?> param) {

        System.out.println("\n##### NBA002SVC param : " + param + "\n");

        NBA00100Out out = new NBA00100Out();

        //서비스기능
        String svcFn = (String)param.get("svcFn");

        String prpsNo = (String)param.get("prpsNo");
        

        PrpsInfoVO  prpsInfoVO = tbmsapl001DM.selectOneTBMSANB001a(prpsNo);

        //청약서발행
        if("prodDesdIsue".equalsIgnoreCase(svcFn)) {

            Map<String, String> dmParam = new HashMap<String, String>();

            dmParam.put("ppsdsnNo", prpsNo);
            dmParam.put("progSt",  "청약서발행요청중");

            int upCnt = tbmsapl001DM.updateOneTBMSANB001a(dmParam);
            System.out.println("upCnt:" + upCnt);

            // 상품MSA에 메시지 전송

            if (prpsInfoVO != null) {
                prpsInfoVO.setEventType("ProductDesdRequested");
            }
            
            publishEvent.publish(prpsInfoVO);

        }

        if (prpsInfoVO != null) {
            BeanUtils.copyProperties(prpsInfoVO, out);
        }


        return out;
    }
}

