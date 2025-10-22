package ezinsurance.jpa;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.beans.BeanUtils;

import ezinsurance.PlanApplication;
import ezinsurance.event.PlanSaved;
import ezinsurance.support.external.ProductService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import lombok.Data;

//가입설계
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Entity
@Table(name="TBMSAPL001")
public class Plan {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ppsdsn_no", length=15)
    String ppsdsnNo      ;    //가입설계번호
    
    @Column(name = "ppsdsn_dt", length=8)
    String ppsdsnDt      ;    //가입설계일자

    @Column(name = "prdcd", length=9)
    String prdcd          ;    //상품코드

    @Column(name = "prdnm", length=100)
    String prdnm          ;    //상품명

    @Column(name = "cust_no", length=9)
    String custNo        ;    //고객번호

    @Column(name = "cust_nm", length=50)
    String custNm        ;    //고객명

    @Column(name = "slct_plnr_eno", length=10)
    String slctPlnrEno  ;    //모집설계사사원번호
    
    @Column(name = "slct_plnr_nm", length=30)
    String slctPlnrNm   ;    //모집설계사사원명
    
    @Column(name = "slct_dof_org_no", length=6)
    String slctDofOrgNo;    //모집지점조직번호
    
    @Column(name = "slct_dof_org_nm", length=30)
    String slctDofOrgNm;    //모집지점조직명
    
    @Column(name = "ins_prd", length=20)
    String insPrd        ;    //보험기간
    
    @Column(name = "pm_prd", length=20)
    String pmPrd         ;    //납입기간
    
    @Column(name = "pm_cyl", length=20)
    String pmCyl         ;    //납입주기

    BigDecimal sprm       ;    //합계보험료
    
    @Column(name = "ent_amt")
    BigDecimal entAmt    ;    //가입금액

    @Column(name = "prod_desd_isue_dt", length=8)
    String prodDesdIsueDt;  //상품설명서발행일자

    @Column(name = "prog_st", length=20)
    String progSt        ;    //진행상태

    @Column(name = "reg_dtm")
    private LocalDateTime regDtm;
    /*
    
    @PostLoad: 해당 엔티티를 새로 불러오거나 refresh 한 이후.
    @PrePersist: 해당 엔티티를 저장하기 이전
    @PostPersist: 해당 엔티티를 저장한 이후
    @PreUpdate: 해당 엔티티를 업데이트 하기 이전
    @PostUpdate: 해당 엔티티를 업데이트 한 이후
    @PreRemove: 해당 엔티티를 삭제하기 이전
    @PostRemove: 해당 엔티티를 삭제한 이후
    */

    @PrePersist
    public void onPrePersist(){
        
        System.out.println("\n\n##### onPrePersist :" + this.toString()+  "\n\n");

        this.setPpsdsnNo(getCurDtm());

        //가입금액체크
        Map<String, String> param = new HashMap<>();
        param.put("svcId", "PDA001SVC");
        param.put("svcFn", "chkProduct");

        param.put("prdcd", getPrdcd());
        param.put("entAmt",getEntAmt()==null? "" : getEntAmt().toString());

        this.regDtm = LocalDateTime.now();
        
        try {
        
            PlanApplication.applicationContext.getBean(ProductService.class).chkProduct(param);
        
        }catch(Exception e) {
            //throw new RuntimeException("가입조건 체크 오류 :: "+e.getLocalizedMessage());

            e.printStackTrace();
        }

        
    }

    @PostPersist
    public void onPostPersist(){

        System.out.println("\n\n##### onPostPersist :" + this.toString()+  "\n\n");

        PlanSaved planSaved = new PlanSaved();
        BeanUtils.copyProperties(this, planSaved);
        planSaved.publishAfterCommit();

        /*
        ProductDesdRequested productDesdRequested = new ProductDesdRequested();
        BeanUtils.copyProperties(this, productDesdRequested);
        productDesdRequested.publishAfterCommit();
        */

    }

    @PostUpdate // 해당 엔티티를 업데이트 한 이후
    public void onPostUpdate(){
        System.out.println("\n\n##### onPostUpdate :" + this.toString()+  "\n\n");


    }

    public String getCurDtm() {
        SimpleDateFormat simpledateformat= new SimpleDateFormat( "yyyyMMddHHmmss");
        Calendar calendar= Calendar.getInstance();
        simpledateformat.setCalendar( calendar);
        String s= simpledateformat.format( simpledateformat.getCalendar().getTime());
        return s;
    }


}
