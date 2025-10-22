package ezinsurance;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.beans.BeanUtils;
import ezinsurance.support.util.DateUtils;
import java.math.BigDecimal;
import lombok.Data;
import org.springframework.util.StringUtils;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Entity
@Table(name="TBMSAPA001")
public class Payment {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name="prps_no"     , length=14) private String prpsNo    ; //청약번호
    @Column(name="act_dcd"     , length=2)  private String actDcd    ; //계좌구분코드
    @Column(name="cust_no"     , length=9)  private String custNo    ; //고객번호
	@Column(name="cust_nm"     , length=40) private String custNm    ; //고객명
    @Column(name="finin_cd"    , length=3)  private String fininCd   ; //금융기관코드
    @Column(name="finin_nm"    , length=50) private String fininNm   ; //금융기관명
    @Column(name="act_no"      , length=50) private String actNo     ; //계좌번호
    @Column(name="achd_nm"     , length=40) private String achdNm    ; //예금주명
    @Column(name="act_stcd"    , length=2)  private String actStcd   ; //계좌상태코드
    @Column(name="answ_cd"     , length=4)  private String answCd    ; //응답코드
    @Column(name="sta_vrfc_dtm", length=14) private String staVrfcDtm; //상태확인일시
    @Column(name="pay_amt"     , length=15) private BigDecimal payAmt; //결제금액
    @Column(name="pay_dtm"     , length=14) private String payDtm    ; //결제일시

    @PrePersist
    public void onPrePersist(){
        
        //십만원초과시 잔액부족
        BigDecimal balnace = new BigDecimal("100000");
        if(payAmt.compareTo(balnace) > 0 ) {
            throw new RuntimeException("잔액부족 초과");
        }

		this.setActStcd("00");  //정상
		this.setAnswCd("0000"); //정상
        this.setStaVrfcDtm(DateUtils.getCurDtm());
		this.setPayDtm(DateUtils.getCurDtm());
	}
	
	
    @PostPersist
    public void onPostPersist(){
        PayApproved payApproved = new PayApproved();
        BeanUtils.copyProperties(this, payApproved);
        
        payApproved.publishAfterCommit();

        /*
        PayCanceled payCanceled = new PayCanceled();
        BeanUtils.copyProperties(this, payCanceled);
        payCanceled.publishAfterCommit();
        */

    }


}
