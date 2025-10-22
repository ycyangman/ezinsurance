package ezinsurance.event;

import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper=false)
public class PlanSaved extends AbstractEvent {

    String ppsdsnNo      ;    //가입설계번호
    String ppsdsnDt      ;    //가입설계일자
    String prdcd          ;    //상품코드
    String prdnm          ;    //상품명
    String custNo        ;    //고객번호
    String custNm        ;    //고객명
    String slctPlnrEno  ;    //모집설계사사원번호
    String slctPlnrNm   ;    //모집설계사사원명
    String slctDofOrgNo;    //모집지점조직번호
    String slctDofOrgNm;    //모집지점조직명
    String insPrd        ;    //보험기간
    String pmPrd         ;    //납입기간
    String pmCyl         ;    //납입주기
    BigDecimal sprm       ;    //합계보험료
    BigDecimal entEmt    ;    //가입금액
    String prodDesdIsueDt;  //상품설명서발행일자
    String progSt        ;    //진행상태
}
