package ezinsurance;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ezinsurance.jpa.Product;
import ezinsurance.jpa.ProductRepository;
import ezinsurance.support.DefaultDTO;
import ezinsurance.support.Executable;
import ezinsurance.svc.PDA001SVC;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

 @RestController
 public class ProductController {


    //@Autowired
    //private PDA001SVC pda001svc;

    @Autowired
    private ProductRepository productRepository;


    @RequestMapping(value = "/products/online", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> doProduct(@RequestBody HashMap<String, String> userMap) throws Exception {


        System.out.println("\n\n##### doProduct userMap : " + userMap + "\n\n");
		
        Map<String, Object> result = new HashMap<>();

		String  svcId = userMap.get("svcId"); // 서비스ID
		//String  svcFn = userMap.get("svcFn"); // 서비스기능

        DefaultDTO dto = new DefaultDTO();

        Executable<?> svc = (Executable<?>)ProductApplication.applicationContext.getBean(svcId);

        dto = (DefaultDTO)svc.execute(userMap);


		result.put("data", dto);
        

		return ResponseEntity.ok().body(result);

    }

    
    @RequestMapping(value = "/products/chkProduct", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> chkProduct(@RequestBody HashMap<String, String> userMap) throws Exception {


        System.out.println("\n##### chkProduct userMap : " + userMap + "\n");
		
		String  svcId = userMap.get("svcId"); // 서비스ID
		String  svcFn = userMap.get("svcFn"); // 서비스기능

        List<Product> products = new ArrayList<Product>();

        BigDecimal entAmt = BigDecimal.ZERO;
        String prdcd  = userMap.get("prdcd");
        String strEntAmt = userMap.get("entAmt");
        
        
        products = productRepository.findByPrdcd(prdcd);

        if( !ObjectUtils.isEmpty(products)) {
            Product prodcut = products.get(0);

            System.out.println("\n\n##### findByPrdcd :: " + prodcut.toString() + "\n\n");

            BigDecimal maxEntAmt = prodcut.getMaxEntAmt();

            if("chkProduct".equals(svcFn)) {

                if(!StringUtils.isEmpty(strEntAmt)) {
                    entAmt = new BigDecimal(strEntAmt);

                    if(entAmt.compareTo(maxEntAmt) > 0 ) {
                        throw new Exception("가입금액 초과");
                    }

                }

            }
        }


        Map<String, Object> result = new HashMap<>();
		result.put("data", products);
        
		return ResponseEntity.ok().body(result);

    }
    

 }
