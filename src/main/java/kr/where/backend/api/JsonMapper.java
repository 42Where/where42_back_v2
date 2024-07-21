package kr.where.backend.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Arrays;
import java.util.List;

import kr.where.backend.api.exception.JsonException;

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
            System.out.println(e.getMessage());
            throw new JsonException.DeserializeException();
        }
    }

    public static String convertJsonForm(final List<?> requestBody) {
        try {
            return OBJECT_MAPPER.writeValueAsString(requestBody);
        } catch(JsonProcessingException e) {
            throw new JsonException.DeserializeException();
        }
    }
}
