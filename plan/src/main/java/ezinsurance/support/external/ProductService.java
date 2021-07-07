
package ezinsurance.support.external;

import java.util.HashMap;
import java.util.Map;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

//api.url.product ==> http://localhost:8081
@FeignClient(name="product", url="${api.url.product}")
public interface ProductService {

    @RequestMapping(method= RequestMethod.POST, path="/products/online", produces = "application/json")
    public Map<String, Object> callService(@RequestBody Map<String, String> userData);

    @RequestMapping(method= RequestMethod.POST, path="/products/chkProduct", produces = "application/json")
    public Map<String, Object> chkProduct(@RequestBody Map<String, String> userData);

}