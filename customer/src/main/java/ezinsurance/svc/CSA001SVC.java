package ezinsurance.svc;

import ezinsurance.io.CSA00100Out;
import ezinsurance.support.ExecuteService;
import ezinsurance.support.util.FwkUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ezinsurance.dm.TBMSACM010DM;
import ezinsurance.vo.CustomerVO;

import java.util.HashMap;

@Service("CSA001SVC")
public class CSA001SVC extends ExecuteService<CSA00100Out> {
    

    @Autowired
    private TBMSACM010DM tbmsacm010DM;

    public CSA00100Out execute(HashMap<?, ?> param) {

        System.out.println("\n##### CSA00100Out param : " + param + "\n");

        CSA00100Out out = new CSA00100Out();

        //서비스기능
        String svcFn = (String)param.get("svcFn");

        if("saveCustomer".equalsIgnoreCase(svcFn)) {

            String jsonStr= FwkUtils.toJson(param);

            CustomerVO customerInfo = FwkUtils.jsonToObject(jsonStr, CustomerVO.class);

            String custNo = tbmsacm010DM.selectMaxCustNo();
            customerInfo.setCustNo(custNo);
            System.out.println("\n##### customerInfo : " + customerInfo + "\n");

            int saveCnt = tbmsacm010DM.insertTBMSACM010a(customerInfo);

            //CustomerVO custInfo = tbmsacm010DM.selectOneTBMSACM010a(custNo);
            BeanUtils.copyProperties(customerInfo, out);

        }
        return out;
    }
}
