package ezinsurance.svc;

import java.util.HashMap;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ezinsurance.bm.ProductBM;
import ezinsurance.io.PDA00200Out;
import ezinsurance.support.ExecuteService;
import ezinsurance.vo.ProductVO;

@Service("PDA002SVC")
public class PDA002SVC extends ExecuteService<PDA00200Out> {
    
    @Autowired
    private ProductBM  productBM;

    public PDA00200Out execute(HashMap<?, ?> param) {

        PDA00200Out out = new PDA00200Out();

        String svcFn = (String)param.get("svcFn"); // 서비스기능

        //인수조건체크
        if("chkProduct".equalsIgnoreCase(svcFn)) {
        
            ProductVO product = productBM.checkProduct(param);

            if(product != null) {
                BeanUtils.copyProperties(product, out);
            }
        }

        return out;

    }
    


}
