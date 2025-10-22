package ezinsurance.io;

import ezinsurance.support.DefaultDTO;
import ezinsurance.vo.CustomerVO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper=false)
public class CSA00100Out extends DefaultDTO {

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

    List<CustomerVO> customerList;

}
