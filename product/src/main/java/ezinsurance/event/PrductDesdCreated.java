package ezinsurance.event;

import lombok.Data;

@Data
public class PrductDesdCreated extends AbstractEvent {

    private Long id;

    String ppsdsnNo      ;    //가입설계번호
    String prdcd         ;    //상품코드
    String prdnm         ;    //상품명
    String custNo        ;    //고객번호
    String custNm        ;    //고객명
    
    public PrductDesdCreated(){
        super();
    }


}
