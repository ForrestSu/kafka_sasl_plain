package com.xunce.demo.examples;

import java.util.Map;

import org.apache.kafka.common.serialization.Deserializer;

public class BytesDeserializer implements Deserializer<byte[]> {
    @Override
    public byte[] deserialize(final String topic, byte[] data) {
        System.out.println("topic => " + topic);
        return data;
    }
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // TODO Auto-generated method stub
    }
    @Override
    public void close() {
        // TODO Auto-generated method stub
    }
}