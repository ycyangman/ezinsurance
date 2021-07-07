package ezinsurance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import ezinsurance.jpa.MypageRepository;
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
public class MypageController {
    
    @Autowired
	MypageRepository mypageRepository;
    

    @RequestMapping(value = "/mypages/online", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> doMypage(@RequestBody HashMap<String, String> userMap) {
        
		
        System.out.println("\n\n##### doMypage userMap : " + userMap + "\n\n");

		Map<String, Object> result = new HashMap<>();

		String  svcId = userMap.get("svcId"); // 서비스ID
		String  svcFn = userMap.get("svcFn"); // 업무ID

		DefaultDTO dto = new DefaultDTO();

        Executable<?> svc = (Executable<?>)MyinsuranceApplication.applicationContext.getBean("MPA001SVC");

        dto = (DefaultDTO)svc.execute(userMap);


		result.put("data", dto);
        

		return ResponseEntity.ok().body(result);
    }
}
