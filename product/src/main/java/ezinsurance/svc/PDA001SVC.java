package ezinsurance.svc;

import java.util.HashMap;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ezinsurance.dm.TBMSAPD001DM;
import ezinsurance.io.PDA00100Out;
import ezinsurance.support.ExecuteService;
import ezinsurance.vo.ProductVO;

@Service("PDA001SVC")
public class PDA001SVC extends ExecuteService<PDA00100Out> {
    
    @Autowired
    private TBMSAPD001DM  tbmsapd001DM;

    public PDA00100Out execute(HashMap<?, ?> param) {

        PDA00100Out out = new PDA00100Out();

        String prdcd = (String)param.get("prdcd");

        ProductVO product = tbmsapd001DM.selectOneTBMSAPD001a(prdcd);

        if(product != null) {
            BeanUtils.copyProperties(product, out);
        }

        return out;

    }
    


}
