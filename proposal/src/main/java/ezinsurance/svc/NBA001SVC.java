package ezinsurance.svc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import ezinsurance.support.ExecuteService;
import ezinsurance.ProposalApplication;
import ezinsurance.io.NBA00100Out;
import ezinsurance.vo.PrpsInfoVO;
import ezinsurance.dm.TBMSANB001DM;

@Service("NBA001SVC")
public class NBA001SVC extends ExecuteService<NBA00100Out>{
    
    @Autowired
    private TBMSANB001DM  tbmsapl001DM;

    public NBA00100Out execute(HashMap<?, ?> param) {

        System.out.println("\n##### PLA001SVC param : " + param + "\n");

        NBA00100Out out = new NBA00100Out();

        //서비스기능
        String svcFn = (String)param.get("svcFn");

        String prpsNo = (String)param.get("prpsNo");

        
        if("".equalsIgnoreCase(svcFn)) {

            //TODO
        }

        PrpsInfoVO  prpsInfo = tbmsapl001DM.selectOneTBMSANB001a(prpsNo);

        if (prpsInfo != null) {
            BeanUtils.copyProperties(prpsInfo, out);
        }

        String custNo = (String)param.get("custNo");
        if(!StringUtils.isEmpty(custNo)) {

            List<PrpsInfoVO>  prpsInfoList = tbmsapl001DM.selectListTBMSANB001a(custNo);

            out.setPrpsInfoList(prpsInfoList);
        }


        return out;
    }
}

