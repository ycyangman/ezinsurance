package ezinsurance.vo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ezinsurance.support.DefaultDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

//설계보험
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper=false)
public class PlanInsVO extends DefaultDTO{
    private String dsnInsNo    ; // 설계보험번호
    private String ppsdsnNo    ; // 가입설계번호
    private String inscd       ; // 보험코드
    private String insnm       ; // 보험명
    private String insDcd      ; // 보험구분코드
    private String insContDt   ; // 보험계약일자
    private String insPrd      ; // 보험기간(명)
    private String insPrdTpcd  ; // 보험기간유형코드
    private Integer insPrdTypVl ; // 보험기간유형값
    private Integer insPrdYcnt  ; // 보험기간년수
    private String pmPrdTpcd   ; // 납입기간유형코드
    private String pmPrd       ; // 납입기간
    private Integer pmPrdMcnt   ; // 납입기간개월수
    private String pmCylCd     ; // 납입주기코드
    private String pmCyl       ; // 납입주기명(명)
    private String expiDt      ; // 만기일자
    private BigDecimal entAmt  ; // 가입금액
    private BigDecimal prm     ; // 보험료
    private BigDecimal nprn    ; // 순보험료
    private Integer insEntAge  ; // 보험가입연령
    private String insStcd     ; // 보험상태코드
    private String insStaDtlCd ; // 보험상태상세코드
    private String trtTpcd     ; // 특약유형코드

}
