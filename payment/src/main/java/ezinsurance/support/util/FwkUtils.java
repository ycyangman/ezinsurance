package ezinsurance.support.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class FwkUtils {
    
    public static <T> T jsonToObject(String json, Class<T> type ){
        ObjectMapper objectMapper = new ObjectMapper();
        T obj = null;

        try {
            obj = (T)objectMapper.readValue(json, type);
        } catch (Exception e) {
            throw new RuntimeException("JSON format exception", e);
        }

        return obj;
    }

    public static <T> T jsonToObject(String json, String path, Class<T> type ){
        ObjectMapper objectMapper = new ObjectMapper();
                
        T obj = null;

        try {

            JsonNode jsonNode = objectMapper.readTree(json);
            JsonNode data = jsonNode.path(path);

            obj = (T)objectMapper.treeToValue(data, type);

        } catch (Exception e) {
            throw new RuntimeException("JSON format exception", e);
        }

        return obj;
    }

    public static String toJson(Object object){
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
