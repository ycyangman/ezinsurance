package ezinsurance.bm;

import java.math.BigDecimal;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ezinsurance.dm.TBMSAPD001DM;
import ezinsurance.vo.ProductVO;

@Component
public class ProductBM {
    
    @Autowired
    private TBMSAPD001DM  tbmsapd001DM;
    
    public ProductVO getProductInfo(String prdcd) {

        return tbmsapd001DM.selectOneTBMSAPD001a(prdcd);

    }

    public ProductVO checkProduct(HashMap<?, ?> userMap) {
        
        System.out.println("\n##### ProductBM.checkProduct calling :: " + userMap.toString() + "\n");

        BigDecimal entAmt = BigDecimal.ZERO;
        String prdcd     = (String)userMap.get("prdcd");
        String strEntAmt = (String)userMap.get("entAmt");

        ProductVO  prodcut = getProductInfo(prdcd);

        if( prodcut != null) {

            System.out.println("\n##### checkProduct :: " + prodcut.toString() + "\n");

            BigDecimal maxEntAmt = prodcut.getMaxEntAmt();

            if(!StringUtils.isEmpty(strEntAmt)) {
                entAmt = new BigDecimal(strEntAmt);
                
                if(entAmt.compareTo(maxEntAmt) > 0 ) {
                    throw new RuntimeException("가입금액 초과");
                }

            }

        }        

        return prodcut;

    }
}
