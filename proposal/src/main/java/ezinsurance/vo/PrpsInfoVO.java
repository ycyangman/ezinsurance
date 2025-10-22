package ezinsurance.vo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ezinsurance.support.DefaultDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

//청약
@Data
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrpsInfoVO extends DefaultDTO{

    String prpsNo;   //청약번호
    String prpsDt;   //청약일자
    String prdcd;    //상품코드
    String prdnm;    //상품명
    String custNo;    //고객번호
    String custNm;    //고객명
    String contNo;    //계약번호
    String contDt;    //계약일자
    String contStcd;  //계약상태코드
    String contStnm;  //계약상태명
    String prpsStcd;  //청약상태코드
    String prpsStnm;  //청약상태명
    String pmMcd;     //납입방법코드
    String pmCylCd;   //납입주기코드
    BigDecimal sprm;    //합계보험료
    BigDecimal entAmt;  //가입금액
    BigDecimal rlpmPrm; //실납입보험료
    String ppsdsnNo;    //가입설계번호
    String dpsDt;         //입금일자
    String prpsdIsueDt;   //청약서발행일자
    String slctPlnrEno;   //모집설계사사원번호
    String slctPlnrNm ;   //모집설계사사원명
    String slctDofOrgNo;  //모집지점조직번호
    String slctDofOrgNm;  //모집지점조직명
}
