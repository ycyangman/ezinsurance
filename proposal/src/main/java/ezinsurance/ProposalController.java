package ezinsurance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ezinsurance.support.DefaultDTO;
import ezinsurance.support.Executable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

 @RestController
 public class ProposalController {


    @RequestMapping(value = "/proposals/online", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> doProposal(@RequestBody HashMap<String, String> userMap) {
        
		
        System.out.println("\n##### doPlan doProposal : " + userMap + "\n");

		Map<String, Object> result = new HashMap<>();

		String  svcId = userMap.get("svcId"); // 서비스ID
		String  svcfn = userMap.get("svcFn"); // 업무기능

		DefaultDTO dto = new DefaultDTO();

        Executable<?> svc = (Executable<?>)ProposalApplication.applicationContext.getBean(svcId);

        dto = (DefaultDTO)svc.execute(userMap);


		result.put("data", dto);
        

		return ResponseEntity.ok().body(result);
    }

 }
