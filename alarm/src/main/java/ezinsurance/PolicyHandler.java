package ezinsurance;

import ezinsurance.config.kafka.KafkaProcessor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class PolicyHandler{
    @Autowired MsgRepository msgRepository;

 
    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){

        System.out.println("\n\n##### eventString : " + eventString + "\n\n");

        HashMap<String, Object> map = jsonToObject(eventString, HashMap.class);
    

        sendMsg(map);
    }


    private void sendMsg(HashMap<String, Object> map) {
        Msg msg = new Msg();

        String custNo = (String)map.get("custNo");
        String custNm = (String)map.get("custNm");
        String phoneNo= (String)map.get("phoneNo");

        String eventType= (String)map.get("eventType");
        String message = "고객 업무 진행사항 : " + eventType;

        msg.setCustNo(custNo);
        msg.setCustNm(custNm);
        msg.setPhoneNo(phoneNo);
        msg.setMessage(message);
        
        SimpleDateFormat defaultSimpleDateFormat = new SimpleDateFormat("YYYY.MM.dd HH:mm:ss");
        msg.setSendDtm(defaultSimpleDateFormat.format(new Date()));

        msgRepository.save(msg);
    }


    public <T> T jsonToObject(String json, Class<T> type ){
        ObjectMapper objectMapper = new ObjectMapper();
        T obj = null;

        try {
            obj = (T)objectMapper.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException("JSON format exception", e);
        }

        return obj;
    }

}
