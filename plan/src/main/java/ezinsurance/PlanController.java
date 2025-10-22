package ezinsurance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ezinsurance.support.DefaultDTO;
import ezinsurance.support.Executable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

 @RestController
 public class PlanController {

    @RequestMapping(value = "/plans/online", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> doPlan(@RequestBody HashMap<String, Object> userMap) {
        
		
        System.out.println("\n##### doPlan userMap : " + userMap + "\n");

		Map<String, Object> result = new HashMap<>();

		String  svcId = (String)userMap.get("svcId"); // 서비스ID
		String  svcfn = (String)userMap.get("svcFn"); // 업무기능

		DefaultDTO dto = new DefaultDTO();

        Executable<?> svc = (Executable<?>)PlanApplication.applicationContext.getBean(svcId);

        dto = (DefaultDTO)svc.execute(userMap);


		result.put("data", dto);
        

		return ResponseEntity.ok().body(result);
    }


 }
