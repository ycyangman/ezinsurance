package ezinsurance.vo;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ezinsurance.support.DefaultDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

//설계관계자
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@EqualsAndHashCode(callSuper=false)
public class PlanRelsVO extends DefaultDTO{

    private String  ppsdsnNo      ;   //가입설계번호
    private String  custContRelcd ;   //고객계약관계코드
    private String  custContRelnm ;   //고객계약관계명
    private String  custNo        ;   //고객번호
    private Integer relpSeq       ;   //관계자순번
    private String  custNm        ;   //고객명
    private Integer age           ;   //연령
    private String  gndrCd        ;   //성별코드
    private String  insJobCd      ;   //보험직업코드
    private String  insJobNm      ;   //보험직업명
    private String  lastRskGcd    ;   //최종위험등급코드
    private String  vhclKcd       ;   //차량종류코드
    private String  pinsdCustRelcd;   //주피보험자고객관계코드
    private String  insdCustRelcd ;   //피보험자고객관계코드
    private String  hobyCd        ;   //취미코드
    private String  rskGcd        ;   //위험등급코드
    private String  drvgJobCd     ;   //운전직업코드

}
