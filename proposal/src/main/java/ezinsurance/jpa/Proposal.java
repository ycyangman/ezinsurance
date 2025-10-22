package ezinsurance.jpa;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;

import ezinsurance.ProposalApplication;
import ezinsurance.event.ProposalSaved;
import ezinsurance.support.util.DateUtils;

import java.util.List;
import java.util.Calendar;
import java.util.Date;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import lombok.Data;

@Data
@Entity
@Table(name="TBMSANB001")
public class Proposal {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "prps_no", length=14)
    String prpsNo;            //청약번호

    @Column(name = "prps_dt", length=8)
    String prpsDt;            //청약일자

    @Column(name = "prdcd", length=9)
    String prdcd;    //상품코드

    @Column(name = "prdnm", length=100)
    String prdnm;    //상품명

    @Column(name = "cust_no", length=9)
    String custNo;    //고객번호

    @Column(name = "cust_nm", length=50)
    String custNm;    //고객명

    @Column(name = "cont_no", length=13)
    String contNo;            //계약번호

    @Column(name = "cont_dt", length=8)
    String contDt;            //계약일자

    @Column(name = "cont_stcd", length=2)
    String contStcd;          //계약상태코드

    @Column(name = "cont_stnm", length=30)
    String contStnm;  //계약상태명
    
    @Column(name = "prps_stcd", length=2)
    String prpsStcd;          //청약상태코드

    @Column(name = "prps_stnm", length=30)
    String prpsStnm;  //청약상태명

    @Column(name = "pm_mcd", length=2)
    String pmMcd;             //납입방법코드

    @Column(name = "pm_cyl_cd", length=2)
    String pmCylCd;          //납입주기코드

    BigDecimal sprm;    //합계보험료

    @Column(name = "ent_amt")
    BigDecimal entAmt;    //가입금액

    @Column(name = "rlpm_prm")
    BigDecimal rlpmPrm;           //실납입보험료

    @Column(name = "ppsdsn_no", length=15)
    String ppsdsnNo;    //가입설계번호

    @Column(name = "dps_dt", length=8)
    String dpsDt;             //입금일자

    @Column(name = "prpsd_isue_dt", length=8)
    String prpsdIsueDt;      //청약서발행일자

    @Column(name = "slct_plnr_eno", length=10)
    String slctPlnrEno  ;    //모집설계사사원번호

    @Column(name = "slct_plnr_nm", length=50)
    String slctPlnrNm   ;    //모집설계사사원명

    @Column(name = "slct_dof_org_no", length=6)
    String slctDofOrgNo;    //모집지점조직번호

    @Column(name = "slct_dof_org_nm", length=50)
    String slctDofOrgNm;    //모집지점조직명

    @Transient
    private String actDcd    ; //계좌구분코드

    @Transient
    private String fininCd   ; //금융기관코드

    @Transient
    private String fininNm   ; //금융기관명

    @Transient
    private String actNo     ; //계좌번호

    @Transient
    private String achdNm    ; //예금주명

    @Transient
    private BigDecimal payAmt; //결제금액


    @PrePersist
    public void onPrePersist(){
        
        System.out.println("\n##### onPrePersist :" + this.toString()+  "\n");

        this.setPrpsNo(DateUtils.getCurDtm()); // 청약번호
        
        ezinsurance.support.external.Payment payment = new ezinsurance.support.external.Payment();
        
        BeanUtils.copyProperties(this, payment);
        System.out.println("\n##### payment :" + payment.toString()+  "\n");


        // mappings goes here
        ProposalApplication.applicationContext.getBean(ezinsurance.support.external.PaymentService.class)
            .makePay(payment);

        this.setPrpsStcd("10");
        this.setPrpsStnm("청약");

        this.setContStcd("30");
        this.setContStnm("초회납입");
        
        this.setDpsDt(DateUtils.getCurrentDate(DateUtils.EMPTY_DATE_TYPE)); //2
        this.setPrpsNo(DateUtils.getCurDtm());

    }


    @PostPersist
    public void onPostPersist(){
        ProposalSaved proposalSaved = new ProposalSaved();
        BeanUtils.copyProperties(this, proposalSaved);
        proposalSaved.publishAfterCommit();

        /*
        PaymentRquested paymentRquested = new PaymentRquested();
        BeanUtils.copyProperties(this, paymentRquested);
        paymentRquested.publishAfterCommit();
        */


    }


}
