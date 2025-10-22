package ezinsurance.jpa;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.Date;
import lombok.Data;

@Data
@Entity
@Table(name="TBMSACM010")
public class Customer {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;

	@Column(name="cust_no", length=9)
	private String custNo; //고객번호
	
	@Column(name="cust_nm", length=50)
	private String custNm; //고객명
	
	@Column(name="cust_dsc_dcd" , length=2)
	private String custDscDcd; //고객실별구분코드
	
	@Column(name="cust_dsc_no", length=44)
	private String custDscNo; //고객식별번호
	
	@Column(name="cust_dcd", length=2)
	private String custDcd; //고객구분코드
	
	@Column(name="cust_stcd", length=2)
	private String custStcd; //고객상태코드
	
	@Column(name="cust_reg_dt", length=8)
	private String custRegDt; //고객등록일자
	
	@Column(name="cust_reg_plnr_eno", length=10)
	private String custRegPlnrEno; //고객등록설계사사원번호
	
	@Column(name="rlpm_vrfc_dt" , length=8)
	private String rlpmVrfcDt; //실명확인일자
	
	@Column(name="bef_asnt_dt", length=8)
	private String befAsntDt; //사전동의일자
	
	@Column(name="gndr_cd" , length=1)
	private String gndrCd; //성별코드
	
	@Column(name="acl_birdt" , length=8)
	private String aclBirdt; //실생년월일
	
	@Column(name="ins_job_cd")
	private String insJobCd; //보험직업코드
	
	@Column(name="ins_job_nm", length=40)
	private String insJobNm; //보험직업명

	@Column(name="vhcl_kcd", length=8)
	private String vhclKcd; //차량종류코드
	
	@Column(name="naty_cd", length=2)
	private String natyCd; //국적코드
	
	@Column(name="pmtr_recvpl_addr_dcd", length=2)
	private String pmtrRecvplAddrDcd; //우편물수령처

    @PostPersist
    public void onPostPersist(){
        
        /*
        CustomerRegistered customerRegistered = new CustomerRegistered();
        BeanUtils.copyProperties(this, customerRegistered);
        customerRegistered.publishAfterCommit();

        CustomerMofied customerMofied = new CustomerMofied();
        BeanUtils.copyProperties(this, customerMofied);
        customerMofied.publishAfterCommit();
        */

    }





}
