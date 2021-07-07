package ezinsurance.svc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ezinsurance.dm.TBMSACM010DM;
import ezinsurance.vo.CustomerVO;

@Service("CSA001SVC")
public class CSA001SVC {
    

    @Autowired
    private TBMSACM010DM tbmsacm010DM;


    public CustomerVO selectCustInfo(String custNo) {

        CustomerVO custInfo = tbmsacm010DM.selectOneTBMSACM010a(custNo);

        System.out.println("\n++++++++++++"+custInfo+"\n");

        return custInfo;

    }
}
