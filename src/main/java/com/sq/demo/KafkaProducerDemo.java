package com.sq.demo;

import java.util.Properties;

import com.sq.demo.examples.Producer;
import com.sq.demo.utils.KafkaPropertiesUtil;

public class KafkaProducerDemo {
	
    /**
     * java -cp ./kafkaDemoAcl-1.0.jar com.xunce.demo.KafkaProducerDemo ./acl.properties
     */
    public static void main(String[] args) 
    {
        if (args.length < 1) {
            System.err.println(">> Usage: java -jar app.jar /etc/uvframe/acl.properties");
            return;
        }
        Properties props = KafkaPropertiesUtil.LoadProperties(args[0]);

        // add into system
        String kers_key = "java.security.auth.login.config";
        System.setProperty(kers_key, props.getProperty(kers_key));
        System.out.println("total load properties, load: " + props.size());
        props.remove(kers_key);


        boolean isAsync = true;
        String topic = props.getProperty("topic", "test");
        System.out.println("publish topic is :" + topic);
        Producer producerThread = new Producer(topic, props, isAsync);
        producerThread.start();
    }
}
