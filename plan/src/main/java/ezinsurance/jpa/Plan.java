package ezinsurance.jpa;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.springframework.beans.BeanUtils;

import ezinsurance.PlanApplication;
import ezinsurance.event.PlanSaved;
import ezinsurance.support.external.ProductService;

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
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name = "ppsdsn_no")
    String ppsdsnNo      ;    //가입설계번호
    
    @Column(name = "ppsdsn_dt")
    String ppsdsnDt      ;    //가입설계일자
    String prdcd          ;    //상품코드
    String prdnm          ;    //상품명

    @Column(name = "cust_no")
    String custNo        ;    //고객번호

    @Column(name = "cust_nm")
    String custNm        ;    //고객명

    @Column(name = "slct_plnr_eno")
    String slctPlnrEno  ;    //모집설계사사원번호
    
    @Column(name = "slct_plnr_nm")
    String slctPlnrNm   ;    //모집설계사사원명
    
    @Column(name = "slct_dof_org_no")
    String slctDofOrgNo;    //모집지점조직번호
    
    @Column(name = "slct_dof_org_nm")
    String slctDofOrgNm;    //모집지점조직명
    
    @Column(name = "ins_prd")
    String insPrd        ;    //보험기간
    
    @Column(name = "pm_prd")
    String pmPrd         ;    //납입기간
    
    @Column(name = "pm_cyl")
    String pmCyl         ;    //납입주기

    BigDecimal sprm       ;    //합계보험료
    
    @Column(name = "ent_amt")
    BigDecimal entAmt    ;    //가입금액

    @Column(name = "prod_desd_isue_dt")
    String prodDesdIsueDt;  //상품설명서발행일자

    @Column(name = "prog_st")
    String progSt        ;    //진행상태

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
        param.put("entAmt",getEntAmt().toString());
        
        try {
        
            PlanApplication.applicationContext.getBean(ProductService.class).chkProduct(param);
        
        }catch(Exception e) {
            throw new RuntimeException("가입조건 체크 오류 :: "+e.getLocalizedMessage());

            //e.printStackTrace();
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
