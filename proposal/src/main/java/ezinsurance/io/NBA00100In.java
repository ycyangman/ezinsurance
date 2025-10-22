package ezinsurance.io;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ezinsurance.support.DefaultDTO;
import ezinsurance.vo.PrpsInfoVO;
import ezinsurance.vo.PrpsInsVO;
import ezinsurance.vo.PrpsRelsVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class NBA00100In extends DefaultDTO {

    private String paymentType   ; //결재방식(1:sync, 2:async)

    private String fininCd   ; //금융기관코드
    private String fininNm   ; //금융기관명
    private String actNo     ; //계좌번호
    private String achdNm    ; //예금주명

    PrpsInfoVO prpsInfo; //청약

    List<PrpsRelsVO> prpsRelsList;  //청약관계자

    List<PrpsInsVO> prpsInsList;  //청약보험

}
