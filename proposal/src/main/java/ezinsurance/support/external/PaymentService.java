
package ezinsurance.support.external;

import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


//@FeignClient(name="payment", url="http://payment:8080")
@FeignClient(name="payment", url="${api.url.payment}")
public interface PaymentService {
    @RequestMapping(method= RequestMethod.POST, path="/payments")
    public void makePay(@RequestBody Payment payment);

    @RequestMapping(method= RequestMethod.POST, path="/payments/chkPayment", produces = "application/json")
    public Map<String, Object> chkPayment(@RequestBody Map<String, String> userData);

}

