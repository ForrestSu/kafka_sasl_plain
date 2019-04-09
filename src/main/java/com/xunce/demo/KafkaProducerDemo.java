/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xunce.demo;

import java.util.Properties;

import com.xunce.demo.examples.Producer;
import com.xunce.demo.utils.KafkaPropertiesUtil;

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
