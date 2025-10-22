package ezinsurance.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown=true)
public class CustomerVO {
    String custNo           ;
    String custNm           ;
    String custDscDcd       ;
    String custDscNo        ;
    String custDcd          ;
    String custStcd         ;
    String custRegDt        ;
    String custRegPlnrEno   ;
    String rlpmVrfcDt       ;
    String befAsntDt        ;
    String gndrCd           ;
    String aclBirdt         ;
    String insJobCd         ;
    String insJobNm         ;
    String vhclKcd          ;
    String natyCd           ;
    String pmtrRecvplAddrDcd;
}
