package com.study.server.core.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.server.core.exception.BusinessException;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MyUtils {

    public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    static {
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static String convertJsonToString(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            throw new BusinessException("Fail convert Json to String!!", e);
        }
    }

    public static <T> T convertStringToJson(String json, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Fail convert String to Json!!", e);
        }
    }

    public static <T> T convertStringToJson(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new BusinessException("Fail convert String to Json!!", e);
        }
    }

    public static <T> void writeResponseJSON(HttpServletResponse response, T body) {
        try {
            response.setContentType(MyMediaType.APPLICATION_JSON_VALUE);
            response.getWriter().print(OBJECT_MAPPER.writeValueAsString(body));
        } catch (IOException e) {
            throw new BusinessException("Fail write response json!!", e);
        }
    }

    public static <T> void writeResponseJSON(HttpServletResponse response, HttpStatus status, T body) {
        response.setStatus(status.value());
        writeResponseJSON(response, body);
    }

}
