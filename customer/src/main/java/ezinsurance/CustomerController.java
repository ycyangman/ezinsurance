package ezinsurance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
//import ezinsurance.support.config.kafka.Producer;
import ezinsurance.svc.CSA001SVC;
import ezinsurance.vo.CustomerVO;

 @RestController
 public class CustomerController {

    //@Value(value = "${cloud.stream.kafka.binder.brokers}")
    //@Value(value = "${kafka.bootstrapServers}")
    //private String kafkBroker;

    /*
    private final Producer producer;

    @Autowired
    CustomerController(Producer producer) {
        this.producer = producer;
    }

    */
    
    @Autowired
    private CSA001SVC csa001svc;

    //게이트웨이 설정에 맞춰 조절해야 함.
    @RequestMapping(value = "/customers/online/{custNo}", method = RequestMethod.GET)
    public ResponseEntity<Map<String, Object>> doCustomers(@PathVariable("custNo") String custNo) {
        
		System.out.println("\n\n##### getCustomers custNo : " + custNo + "\n\n");

        CustomerVO custInfo = csa001svc.selectCustInfo(custNo);

        Map<String, Object> result = new HashMap<>();
		result.put("data", custInfo);
		return ResponseEntity.ok().body(result);
    }
    
	/*
    @RequestMapping(value = "/customers/kafka/{kafkaMessage}", method = RequestMethod.POST)
    public ResponseEntity<Map<String, Object>> produceKafkaMessage(@PathVariable("kafkaMessage") String kafkaMessage) {
        
        System.out.println("\n\n##### kafkBroker : " + kafkBroker + "\n\n");

        this.producer.sendMessage(kafkaMessage);

        
        KafkaMessageDTO inputKafkaMessageDTO = new KafkaMessageDTO();

        inputKafkaMessageDTO.setKafkaMessage1(kafkaMessage);

        kafkaMessageService.produceKafkaMessage(inputKafkaMessageDTO);

        return new ResponseEntity<>(HttpStatus.OK);
    }
	*/

 }
