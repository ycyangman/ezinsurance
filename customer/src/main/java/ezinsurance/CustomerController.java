package ezinsurance;

import ezinsurance.support.DefaultDTO;
import ezinsurance.support.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import ezinsurance.support.config.kafka.Producer;
import ezinsurance.svc.CSA001SVC;

 @RestController
 public class CustomerController {

    //@Value(value = "${cloud.stream.kafka.binder.brokers}")
    @Value(value = "${kafka.bootstrapServers}")
    private String kafkBroker;

    private final Producer producer;

    @Autowired
    CustomerController(Producer producer) {
        this.producer = producer;
    }

    @Autowired
    private CSA001SVC csa001svc;

    //게이트웨이 설정에 맞춰 조절해야 함.
    @RequestMapping(value = "/customers/online", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> doCustomers(@RequestBody HashMap<String, Object> userMap) {

        System.out.println("\n##### doPlan userMap : " + userMap + "\n");

        Map<String, Object> result = new HashMap<>();

        String  svcId = (String)userMap.get("svcId"); // 서비스ID
        String  svcfn = (String)userMap.get("svcFn"); // 업무기능

        DefaultDTO dto = new DefaultDTO();

        Executable<?> svc = (Executable<?>)CustomerApplication.applicationContext.getBean(svcId);

        dto = (DefaultDTO)svc.execute(userMap);


        result.put("data", dto);


        return ResponseEntity.ok().body(result);
    }
    

    @RequestMapping(value = "/customers/kafka/{kafkaMessage}", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> produceKafkaMessage(@PathVariable("kafkaMessage") String kafkaMessage) {
        
        System.out.println("\n\n##### kafkBroker : " + kafkBroker + "\n\n");

        this.producer.sendMessage(kafkaMessage);

        /*
        KafkaMessageDTO inputKafkaMessageDTO = new KafkaMessageDTO();

        inputKafkaMessageDTO.setKafkaMessage1(kafkaMessage);

        kafkaMessageService.produceKafkaMessage(inputKafkaMessageDTO);

        */
        return new ResponseEntity<>(HttpStatus.OK);
    }

 }
