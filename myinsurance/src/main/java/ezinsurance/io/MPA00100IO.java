package ezinsurance.io;

import java.util.List;

import ezinsurance.jpa.Mypage;
import ezinsurance.support.DefaultDTO;
import lombok.Data;


@Data
public class MPA00100IO extends DefaultDTO{
    
    String svcDvn;

    List<Mypage> mypages;

}
