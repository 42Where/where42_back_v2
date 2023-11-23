package kr.where.backend.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;
import kr.where.backend.exception.json.JsonException;
import kr.where.backend.exception.json.JsonException.DeserializeException;

public class JsonMapper {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static <T> T mapping(final String jsonBody, final Class<T> classType) {
        try {
            return OBJECT_MAPPER.readValue(jsonBody, classType);
        } catch (JsonProcessingException e) {
            throw new JsonException.DeserializeException();
        }
    }

    public static <T> List<T> mappings(final String jsonBody, final Class<T[]> classType) {
        try {
            return Arrays.asList(OBJECT_MAPPER.readValue(jsonBody, classType));
        } catch (JsonProcessingException e) {
            throw new JsonException.DeserializeException();
        }
    }
}
