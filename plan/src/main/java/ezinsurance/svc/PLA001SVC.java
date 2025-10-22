package ezinsurance.svc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import ezinsurance.PlanApplication;
import ezinsurance.io.PLA00100Out;
import ezinsurance.support.ExecuteService;
import ezinsurance.support.external.ProductService;
import ezinsurance.support.util.FwkUtils;
import ezinsurance.vo.PlanInfoVO;
import ezinsurance.vo.ProductVO;
import ezinsurance.dm.TBMSAPL001DM;

@Service("PLA001SVC")
public class PLA001SVC extends ExecuteService<PLA00100Out>{
    
    @Autowired
    private TBMSAPL001DM  tbmsapl001DM;

    public PLA00100Out execute(HashMap<?, ?> param) {

        System.out.println("\n##### PLA001SVC param : " + param + "\n");

        PLA00100Out out = new PLA00100Out();

        //서비스기능
        String svcFn = (String)param.get("svcFn");

        String ppsdsnNo = (String)param.get("ppsdsnNo");

        //보험료계산
        //상품MSA 보험료계산 서비스호출
        if("calcPrm".equalsIgnoreCase(svcFn)) {

            //가입금액체크

            Map<String, String> svcParam = new HashMap<>();
            svcParam.put("svcId", "PDA002SVC");
            svcParam.put("svcFn", "chkProduct");

            String prdcd  = (String)param.get("prdcd");
            String entAmt = (String)param.get("entAmt");

           
            if(StringUtils.isEmpty(prdcd)) {
                throw new RuntimeException("상품코드 필수입력");
            }

            /*
            if(StringUtils.isEmpty(entAmt)) {
                throw new RuntimeException("가입금액 필수입력");            
            }
            */
           
            svcParam.put("prdcd", prdcd);
            svcParam.put("entAmt", entAmt);
            
            try {
            
                Map<String, Object> outMap = PlanApplication.applicationContext.getBean(ProductService.class).callService(svcParam);

                System.out.println("\n##### PLA002SVC outMap : " + outMap + "\n");
                
                String jsonStr= FwkUtils.toJson(outMap);
                                
                System.out.println("\n##### PLA002SVC FwkUtils.toJson(outMap) : " + jsonStr + "\n");

                ProductVO productInfo = FwkUtils.jsonToObject(jsonStr, "data", ProductVO.class);

                //System.out.println("\n##### PLA002SVC productInfo : " + productInfo.toString() + "\n");

                BeanUtils.copyProperties(productInfo, out);
            
            }catch(Exception e) {
                throw new RuntimeException("보험료계산 오류 :: "+e.getLocalizedMessage());

                //e.printStackTrace();
            }



        }

        return out;
    }
}

