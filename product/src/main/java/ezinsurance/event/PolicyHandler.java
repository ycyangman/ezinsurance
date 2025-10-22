package ezinsurance.event;

import ezinsurance.jpa.ProductDocument;
import ezinsurance.jpa.ProductDocumentRepository;
import ezinsurance.jpa.ProductRepository;
import ezinsurance.support.config.kafka.KafkaProcessor;
import ezinsurance.vo.PlanVO;

import java.util.HashMap;

//import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class PolicyHandler{
    
    @Autowired ProductRepository productRepository;
    @Autowired ProductDocumentRepository productDocumentRepository;


    @StreamListener(KafkaProcessor.INPUT)
    public void whatever(@Payload String eventString){

        System.out.println("\n\n##### eventString : " + eventString + "\n\n");

        HashMap<String, Object> map = jsonToObject(eventString, HashMap.class);

        String eventType = (String)map.get("eventType");
        

        //상설작업요청
        if("ProductDesdRequested".equalsIgnoreCase(eventType)) {

            PlanVO planInfo = jsonToObject(eventString, PlanVO.class);

            ProductDocument prodDesd = new ProductDocument();
            BeanUtils.copyProperties(planInfo, prodDesd);
            
            String  notclCtnt = createProductDesd(planInfo);
            
            prodDesd.setIsueRefNo(planInfo.getPpsdsnNo());
            prodDesd.setNotclCtnt(notclCtnt);
            prodDesd.setNotclCtnt(notclCtnt);
            prodDesd.setFormTpcd("PRD001");
            prodDesd.setFormNm("상품설명서");

            productDocumentRepository.save(prodDesd);
        }

        
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

    public String createProductDesd(PlanVO planInfo ) {

        System.out.println("\nn##### productDesd creating #######\n");

        try{
            Thread.sleep(10000); //10 초지연
        }catch(InterruptedException e){
            e.printStackTrace();
        }

        System.out.println("\nn##### productDesd created #######\n");

        StringBuffer sb = new StringBuffer();
        sb.append("..상품설명서..");
        sb.append("\n가입설계번호:" +planInfo.getPpsdsnNo());
        sb.append("\n상품명:" +planInfo.getPrdnm());
        sb.append("\n보험료:" +planInfo.getSprm());
        sb.append("\n보험기간:" +planInfo.getInsPrd());
        sb.append("\n납입기간:" +planInfo.getPmPrd());
        sb.append("\n납입주기:" +planInfo.getPmCyl());
        sb.append("\n: 상품해지시 환급보험료는 없거나 기납입보험료보다 작을수 있습니다." );

        return sb.toString();
    }
}
