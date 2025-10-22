package ezinsurance.event;

import ezinsurance.jpa.Plan;
import ezinsurance.jpa.PlanRepository;
import ezinsurance.support.config.kafka.KafkaProcessor;
import ezinsurance.support.util.FwkUtils;

import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
public class PolicyHandler{
    @Autowired PlanRepository planRepository;

    /*
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPrductDesdCreated_UpdatePlan(@Payload PrductDesdCreated prductDesdCreated){

        if(!prductDesdCreated.validate()) return;
        // Get Methods


        // Sample Logic //
        System.out.println("\n\n##### listener UpdatePlan : " + prductDesdCreated.toJson() + "\n\n");
    }
    */


    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){
        HashMap<String, Object> map = FwkUtils.jsonToObject(eventString, HashMap.class);

        String eventType = (String)map.get("eventType");
        

        //상설생성완료 상태값=> 상설생성완료
        if("PrductDesdCreated".equalsIgnoreCase(eventType)) {

            Plan planInfo = FwkUtils.jsonToObject(eventString, Plan.class);

            String ppsdsnNo = planInfo.getPpsdsnNo();
            
            List<Plan> plans = planRepository.findByPpsdsnNo(ppsdsnNo);
            if(!ObjectUtils.isEmpty(plans)) {

                Plan plan =plans.get(0);
                plan.setProgSt("상품설명서생성완료");

                planRepository.save(plan);

            }

            
        }

    }


}
