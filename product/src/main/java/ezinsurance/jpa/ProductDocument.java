package ezinsurance.jpa;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;

import ezinsurance.event.PrductDesdCreated;

import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import lombok.Data;

@Data
@Entity
@Table(name="TBMSAPD010")
public class ProductDocument {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name = "isue_no"    ) String isueNo   ; //발행번호
    @Column(name = "isue_ref_no") String isueRefNo; //발행참조번호
    @Column(name = "prdcd"      ) String prdcd;   ;  //상품코드
    @Column(name = "isue_dt"    ) String isueDt   ; //발행일자
    @Column(name = "form_tpcd"  ) String formTpcd ; //양식코드
    @Column(name = "form_nm"    ) String formNm   ; //양식명
    @Column(name = "notcl_ctnt", length=255 ) String notclCtnt; //안내장내용
    @Column(name = "send_yn"    ) String sendYn   ; //전송여부


    //테이블 저장안됨
    @Transient
    String custNo;
    
    @Transient
    String custNm;


    @PrePersist
    public void onPrePersist(){
        
        System.out.println("\n\n##### onPrePersist :" + this.toString()+  "\n\n");

        this.setIsueNo(getCurDtm("yyyyMMddHHmmss"));
        this.setIsueDt(getCurDtm("yyyyMMdd"));

        this.setSendYn("N");
    }

    @PostPersist
    public void onPostPersist(){
        

        //상설결과 카프카 전송
        PrductDesdCreated prductDesdCreated = new PrductDesdCreated();
        BeanUtils.copyProperties(this, prductDesdCreated);
        prductDesdCreated.setPpsdsnNo(this.getIsueRefNo());
        prductDesdCreated.publishAfterCommit();

        /*
        ProposalDesdCreated proposalDesdCreated = new ProposalDesdCreated();
        BeanUtils.copyProperties(this, proposalDesdCreated);
        proposalDesdCreated.publishAfterCommit();
        */


    }

    public String getCurDtm(String format) {
        SimpleDateFormat simpledateformat= new SimpleDateFormat(format);
        Calendar calendar= Calendar.getInstance();
        simpledateformat.setCalendar( calendar);
        String s= simpledateformat.format( simpledateformat.getCalendar().getTime());
        return s;
    }
}
