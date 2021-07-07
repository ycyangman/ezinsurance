package ezinsurance.support.external;

import java.math.BigDecimal;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Payment {

    private String prpsNo    ; //청약번호
    private String actDcd    ; //계좌구분코드
    private String custNo    ; //고객번호
    private String custNm    ; //고객명
    private String fininCd   ; //금융기관코드
    private String fininNm   ; //금융기관명
    private String actNo     ; //계좌번호
    private String achdNm    ; //예금주명
    private String actStcd   ; //계좌상태코드
    private String answCd    ; //응답코드
    private String staVrfcDtm; //상태확인일시
    private BigDecimal payAmt; //결제금액
    private String payDtm    ; //결제일시

}
