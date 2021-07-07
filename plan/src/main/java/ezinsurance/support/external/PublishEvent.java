package ezinsurance.support.external;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeTypeUtils;

import ezinsurance.support.config.kafka.KafkaProcessor;

@Component
public class PublishEvent {
    
    @Autowired
    private KafkaProcessor kafkaProcessor;

    public void publish(String msg) {


        MessageChannel outputChannel = kafkaProcessor.outboundTopic();

        outputChannel.send(MessageBuilder
                .withPayload(msg)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build());

    }

    public void publish(Object object) {

        publish(toJson(object));

    }

    public String toJson(Object object){
        ObjectMapper objectMapper = new ObjectMapper();
        String json = null;

        try {
            json = objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON format exception", e);
        }

        return json;
    }

}
