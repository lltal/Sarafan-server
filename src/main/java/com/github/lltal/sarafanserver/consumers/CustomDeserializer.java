package com.github.lltal.sarafanserver.consumers;

import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.lltal.sarafanserver.dto.KafkaMessageDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomDeserializer implements Deserializer<KafkaMessageDto>
{
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public KafkaMessageDto deserialize(String topic, byte[] data) {
        try {
            if (data == null){
                System.out.println("Null received at deserializing");
                return null;
            }
            String s = new String(data, "UTF-8");
            return objectMapper.readValue(s, KafkaMessageDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
            //throw new SerializationException("Error when deserializing byte[] to KafkaMessageDto");
        }
    }
}
