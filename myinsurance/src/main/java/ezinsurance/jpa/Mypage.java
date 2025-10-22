package ezinsurance.jpa;

import javax.persistence.*;

import ezinsurance.support.util.DateUtils;

import java.util.List;
import lombok.Data;


@Data
@Entity
@Table(name="TBMSAMP001")
public class Mypage {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

    @Column(name = "event_type")
    String eventType;

    @Column(name="cust_no")
    String custNo; //고객번호
    
    @Column(name="cust_nm")
    String custNm; //고객명

    @Column(name = "ppsdsn_no")
    String ppsdsnNo      ;    //가입설계번호
    
    @Column(name = "ppsdsn_dt")
    String ppsdsnDt      ;    //가입설계일자

    String prdcd          ;    //상품코드
    String prdnm          ;    //상품명
        
    @Column(name = "prod_desd_isue_dt")
    String prodDesdIsueDt;  //상품설명서발행일자

    @Column(name = "prog_st")
    String progSt        ;    //진행상태

    @Column(name = "prps_no")
    String prpsNo;            //청약번호

    @Column(name = "prps_dt")
    String prpsDt;            //청약일자
   
    @Column(name = "cont_no")
    String contNo;            //계약번호

    @Column(name = "cont_dt")
    String contDt;            //계약일자

    @Column(name = "prps_stcd")
    String prpsStcd;          //청약상태코드

    @Column(name = "cont_stcd")
    String contStcd;          //계약상태코드

    @Column(name = "prps_stnm")
    String prpsStnm;  //청약상태명

    @Column(name = "cont_stnm")
    String contStnm;  //계약상태명

    @Column(name = "reg_dtm")
    String regDtm; //등록일시

    @Column(name = "chg_dtm")
    String chgDtm; //변경일시

    @PrePersist
    public void onPrePersist(){
        
        System.out.println("\n##### onPrePersist :" + this.toString()+  "\n");

        this.setRegDtm(DateUtils.getCurDtm()); 
        this.setChgDtm(DateUtils.getCurDtm()); 

    }
}
