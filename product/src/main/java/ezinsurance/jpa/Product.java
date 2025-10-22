package ezinsurance.jpa;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;

@Data
@Entity
@Table(name="TBMSAPD001")
public class Product {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    String prdcd          ;  // 상품코드
    String prdnm          ;  // 상품명

    @Column(name = "ins_dcd")
    String insDcd        ;  // 보험구분코드

    @Column(name = "prd_sell_opn_dt")
    String prdSellOpnDt;  // 상품판매개시일자

    @Column(name = "prd_sell_end_dt")
    String prdSellEndDt;  // 상품판매종료일자

    @Column(name = "ins_prd")
    String insPrd;    //보험기간

    @Column(name = "ins_prd_typ_vl")
    Integer insPrdTypVl;  // 보험기간유형값

    @Column(name = "ins_prd_ycnt")
    Integer insPrdYcnt  ;  // 보험기간년수

    @Column(name = "pm_prd_tpcd")
    String pmPrdTpcd    ;  // 납입기간유형코드

    @Column(name = "pm_prd")
    String pmPrd        ;  // 납입기간

    @Column(name = "pm_prd_mcnt")
    Integer pmPrdMcnt   ;  // 납입기간개월수

    @Column(name = "pm_cyl")
    String pmCyl         ;    //납입주기

    @Column(name = "pm_cyl_cd")
    String pmCylCd      ;  // 납입주기코드

    @Column(name = "min_ent_amt")
    BigDecimal minEntAmt;  // 최소가입금액

    @Column(name = "max_ent_amt")
    BigDecimal maxEntAmt;  // 최대가입금액

    @Column(name = "min_ent_age")
    Integer minEntAge    ;  // 최소가입연령

    @Column(name = "max_ent_age")
    Integer maxEntAge    ;  // 최대가입연령

    BigDecimal prm        ;  // 보험료


}
