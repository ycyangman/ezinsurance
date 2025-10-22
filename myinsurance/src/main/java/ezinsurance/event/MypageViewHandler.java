package ezinsurance.event;

import ezinsurance.jpa.Mypage;
import ezinsurance.jpa.MypageRepository;
import ezinsurance.support.config.kafka.KafkaProcessor;
import ezinsurance.support.util.DateUtils;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class MypageViewHandler {


    @Autowired
    private MypageRepository mypageRepository;


    @StreamListener(KafkaProcessor.INPUT)
    public void whenEventOccurred (@Payload Mypage eventInfo) {
        try {

          
            System.out.println("\n\n##### eventInfo : " + eventInfo.toString() + "\n\n");


            String eventType = eventInfo.getEventType();

            if(StringUtils.isEmpty(eventType)) {
                return;
            }

            String custNo = eventInfo.getCustNo();     //고객번혼
            String ppsdsnNo = eventInfo.getPpsdsnNo(); //가설번호
            String prpsNo = eventInfo.getPrpsNo();     //청약번호
            
            //가입설계
            if( "PlanSaved".equalsIgnoreCase(eventType) || "PrductDesdCreated".equalsIgnoreCase(eventType)) {
                
                List<Mypage> mypageList = mypageRepository.findByCustNoAndPpsdsnNo(custNo, ppsdsnNo);

                if(ObjectUtils.isEmpty(mypageList)) {
                    Mypage mypage = new Mypage();
                    BeanUtils.copyProperties(eventInfo, mypage);
    
                    mypageRepository.save(mypage);
                }
    
                for(Mypage mypage : mypageList){
    
                    String progSt = eventInfo.getProgSt();
                    if( "PrductDesdCreated".equalsIgnoreCase(eventType)) {
                        
                        if(StringUtils.isEmpty(progSt)) {
                            progSt = "상품설명서생성완료";
                        }

                        mypage.setProdDesdIsueDt(DateUtils.getCurrentDate(DateUtils.EMPTY_DATE_TYPE));
                        
                    }

                    mypage.setEventType(eventType);
                    mypage.setProgSt(progSt);

                    mypage.setChgDtm(DateUtils.getCurDtm());

                    mypageRepository.save(mypage);
                }
            }

            //청약
            if( "ProposalSaved".equalsIgnoreCase(eventType) || "ProposalDesdCreated".equalsIgnoreCase(eventType)) {
                            
                List<Mypage> mypageList = mypageRepository.findByCustNoAndPpsdsnNo(custNo, prpsNo);

                if(ObjectUtils.isEmpty(mypageList)) {
                    Mypage mypage = new Mypage();
                    BeanUtils.copyProperties(eventInfo, mypage);

                    mypageRepository.save(mypage);
                }

                for(Mypage mypage : mypageList){

                    mypage.setEventType(eventType);

                    if(!StringUtils.isEmpty(eventInfo.getPrpsStcd())) {
                        mypage.setPrpsStcd(eventInfo.getPrpsStcd());
                        mypage.setPrpsStnm(eventInfo.getPrpsStnm());
                    }

                    if(!StringUtils.isEmpty(eventInfo.getContStcd())) {
                        mypage.setContStcd(eventInfo.getContStcd());
                        mypage.setContStnm(eventInfo.getContStnm());
                    }

                    mypage.setChgDtm(DateUtils.getCurDtm());

                    mypageRepository.save(mypage);
                }
            }

            System.out.println("======================================================================================");

        
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}

