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
package com.xunce.demo.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class KafkaPropertiesUtil {
    public static final String TOPIC = "test";
    public static final String KAFKA_SERVER_URL = "192.168.0.223";
    public static final int KAFKA_SERVER_PORT = 9092;
    public static final int KAFKA_PRODUCER_BUFFER_SIZE = 64 * 1024;
    public static final int CONNECTION_TIMEOUT = 100000;
    public static final String CLIENT_ID = "SimpleConsumerDemoClient";

    public static Properties LoadProperties(String file_name) {
        File fprop = new File(file_name);
        if (!fprop.exists()) {
            System.err.println("file: " + file_name + " is not exist!");
            return null;
        }
        boolean has_except = false;
        Properties props = new Properties();
        BufferedInputStream in;
        try {
            in = new BufferedInputStream(new FileInputStream(file_name));
            props.load(in);
        } catch (IOException ioe) {
            has_except = true;
            System.err.println("Properties file: '" + file_name + "' could not be read." + ioe);
        }
        if (has_except) {
            return null;
        }
        return props;
    }
}
