package com.sq.demo;

import java.util.Properties;

import com.sq.demo.examples.Consumer;
import com.sq.demo.utils.KafkaPropertiesUtil;

public class KafkaConsumerDemo {
	
    /**
     * java -cp ./kafkaDemoAcl-1.0.jar com.xunce.demo.KafkaConsumerDemo ./acl.properties
     */
    public static void main(String[] args) {
        if (args.length < 1) {
            System.err.println(">> Usage: java -jar app.jar /etc/uvframe/acl.properties");
            return;
        }
        Properties props = KafkaPropertiesUtil.LoadProperties(args[0]);

        // must be add into system
        String kers_key = "java.security.auth.login.config";
        System.setProperty(kers_key, props.getProperty(kers_key));
        System.out.println("load properties, load: " + props.size());
        props.remove(kers_key);


        System.out.println("start ...");
        String topic = props.getProperty("topic", "test");
        System.out.println("subscrible topic is:" + topic);
        Consumer consumerThread = new Consumer(topic, props);
        consumerThread.start();
        
        System.out.println("main thread is exit!");
    }
}
