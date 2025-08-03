package io.github.orionlibs.core.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONService
{
    private final ObjectMapper objectMapper;


    public JSONService(ObjectMapper objectMapper)
    {
        this.objectMapper = objectMapper;
    }


    public String toJson(Object pojo)
    {
        try
        {
            return objectMapper.writeValueAsString(pojo);
        }
        catch(JsonProcessingException e)
        {
            throw new IllegalStateException("Failed to serialize object to JSON", e);
        }
    }
}
