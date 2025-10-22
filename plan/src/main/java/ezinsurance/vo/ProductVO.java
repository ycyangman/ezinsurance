package ezinsurance.vo;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import ezinsurance.support.DefaultDTO;

import java.math.BigDecimal;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductVO extends DefaultDTO {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    String prdcd        ;  // 상품코드
    String prdnm        ;  // 상품명
    String insDcd       ;  // 보험구분코드
    String prdSellOpnDt ;  // 상품판매개시일자
    String prdSellEndDt ;  // 상품판매종료일자
    String insPrd       ;  // 보험기간
    Integer insPrdTypVl ;  // 보험기간유형값
    Integer insPrdYcnt  ;  // 보험기간년수
    String pmPrdTpcd    ;  // 납입기간유형코드
    String pmPrd        ;  // 납입기간
    Integer pmPrdMcnt   ;  // 납입기간개월수
    String pmCyl        ;  // 납입주기
    String pmCylCd      ;  // 납입주기코드
    BigDecimal minEntAmt;  // 최소가입금액
    BigDecimal maxEntAmt;  // 최대가입금액
    Integer minEntAge   ;  // 최소가입연령
    Integer maxEntAge   ;  // 최대가입연령
    BigDecimal prm      ;  // 보험료

}
