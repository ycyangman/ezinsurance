package ezinsurance.svc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import ezinsurance.support.DefaultDTO;
import ezinsurance.support.Executable;
import ezinsurance.support.ExecuteService;
import ezinsurance.jpa.Mypage;
import ezinsurance.jpa.MypageRepository;
import ezinsurance.io.MPA00100IO;
import org.springframework.beans.factory.annotation.Autowired;


@Service("MPA001SVC")
public class MPA001SVC extends ExecuteService<MPA00100IO>{

    @Autowired
	MypageRepository mypageRepository;
    
    @Override
    public MPA00100IO execute(HashMap<?, ?> param) {

        System.out.println("\n\n##### MPA001SVC param : " + param + "\n\n");


        MPA00100IO out = new MPA00100IO();
        
        System.out.println("\n\n##### getMypages userMap : " + param + "\n\n");
		
		String  type   = (String)param.get("type"); // 
		String  myName = (String)param.get("myName");

        String custNo =  (String)param.get("custNo");
        String ppsdsnNo = (String)param.get("ppsdsnNo");


		List<Mypage> mypages = new ArrayList<>();

		if(myName==null || "".equals(myName)) {
			Iterable<Mypage> mypageIt = mypageRepository.findAll();

			if( mypageIt!= null)
			{
				mypageIt.forEach(mypages::add);
			}
		}
		else {
			mypages = mypageRepository.findByCustNoAndPpsdsnNo(custNo, ppsdsnNo);

		}

        out.setMypages(mypages);
        
        return out;
    }

    
}
