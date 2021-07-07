package ezinsurance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.util.List;
import sun.misc.Unsafe;

 @RestController
 public class PaymentController {

    @Autowired
    PaymentRepository PaymentRepository;

    @RequestMapping(value = "/payments/online", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> doMypage(@RequestBody HashMap<String, String> userMap) {
        
		
        System.out.println("\n\n##### doMypage userMap : " + userMap + "\n\n");

		Map<String, Object> result = new HashMap<>();

		String  svcId = userMap.get("svcId"); // 서비스ID
		String  svcFn = userMap.get("svcFn"); // 업무ID
        
        if("cancelPay".equalsIgnoreCase(svcFn)) {
        
            String  prpsNo = userMap.get("prpsNo"); // 청약번호

            List<Payment> payments = PaymentRepository.findByPrpsNo(prpsNo);
            for(Payment payment : payments){
                
                payment.setStatus("결재취소");

                PaymentRepository.save(payment);
            }

		    result.put("data", "처리완료");
     }
        

		return ResponseEntity.ok().body(result);
    }

    @GetMapping("callMemleak")
    public void callMemleak() {

        try {
            Class unsafeClass = Class.forName("sun.misc.Unsafe");
            Field f = unsafeClass.getDeclaredField("theUnsafe");
            f.setAccessible(true);

            Unsafe unsafe = (Unsafe) f.get(null);

            System.out.println("\n=======callMemleak===========\n");

            try {
                for(;;) {
                    unsafe.allocateMemory(1024*1024);
                }
            } catch (Error e) {
                System.out.println("\n=======killing===========\n");
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 }
