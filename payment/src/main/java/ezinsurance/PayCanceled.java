package ezinsurance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper=false)
public class PayCanceled extends AbstractEvent {

    private String prpsNo    ; //청약번호
    private String custNo    ; //고객번호
    private String custNm    ; //고객명
    private String fininCd   ; //금융기관코드
    private String fininNm   ; //금융기관명
    private String actNo     ; //계좌번호
    private String achdNm    ; //예금주명

    private BigDecimal payAmt; //결제금액
    private String payDtm    ; //결제일시
}
