package ezinsurance;

import ezinsurance.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import ezinsurance.support.util.FwkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class PolicyHandler{
    @Autowired PaymentRepository paymentRepository;



    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventMsg){

        System.out.println("\n\n##### eventMsg : " + eventMsg + "\n\n");

        HashMap<String, Object> map = FwkUtils.jsonToObject(eventMsg, HashMap.class);

        String eventType = (String)map.get("eventType");


        //결재요청
        if("requestPayment".equalsIgnoreCase(eventType)) {

            Payment payment = FwkUtils.jsonToObject(eventMsg, Payment.class);

            paymentRepository.save(payment);
        }


    }


}
