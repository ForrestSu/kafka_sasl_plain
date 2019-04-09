# kafka_kudu_sasl_plain
Demo for kafka with Authentication using SASL/PLAIN, but without install kerberos server


### 1 添加kafka_server_jaas.conf
```
KafkaClient {
        com.sun.security.auth.module.Krb5LoginModule required
        useKeyTab=true
        storeKey=true
        keyTab="/home/xunce/xunce.keytab"
        principal="kafka/hu91@XUNCE.COM"
        useTicketCache=true;
};
```

### 2 使用kafka 命令行工具验证
1  在  slave01 上启动生产者

```
$slave01>  bin/kafka-console-producer.sh --broker-list slave01:9092,master:9092,slave02:9092 --topic sunquan  --producer.config config/producer.properties 
```

2 在 slave02 上启动消费者

```
$slave02>  bin/kafka-console-consumer.sh --bootstrap-server master:9092,slave02:9092,slave01:9092 --topic sunquan  --from-beginning --consumer.config config/consumer.properties 
```

### 3 创建topic
```
$xunce> bin/kafka-topics.sh --create --zookeeper master:2181 --replication-factor 1 --partitions 1 --topic sunquan
```

### 4 如何在集群之外, 部署一个消费者?

**集群内配置**

1 先在集群里 每台机器都添加 hosts 添加一行  
> 192.168.0.191 hu91

2 然后在keytab 文件中增加授权  

```
root> kadmin.local -q 'addprinc -randkey kafka/hu91@XUNCE.COM'
```
3 生成xunce.keytab 文件  

```
xunce> kadmin.local -q "xst -norandkey -k xunce.keytab kafka/hu91@XUNCE.COM"
```
4 然后把生成的xunce.keytab 文件同步到 集群的所有机器    
> scp /usr/local/kafka/xunce.keytab slave01:/usr/local/kafka/xunce.keytab
> scp /usr/local/kafka/xunce.keytab slave02:/usr/local/kafka/xunce.keytab

5 重启集群内的所有 zookeeper 服务:

```
xunce> /usr/local/zookeeper/bin/zkServer.sh stop
xunce> /usr/local/zookeeper/bin/zkServer.sh start
xunce> /usr/local/zookeeper/bin/zkServer.sh status
```

6 重启集群内的所有kafka  

```
cd /usr/local/kafka
停止：xunce> bin/kafka-server-stop.sh config/server.properties
启动：xunce> nohup bin/kafka-server-start.sh config/server.properties &
```

**消费者所在机器-配置**

1 在 hosts 文件中添加如下内容  
> vim /etc/hosts 
```
192.168.0.223 master
192.168.0.224 slave01
192.168.0.225 slave02
192.168.0.191 hu91
```

2 安装kerberos客户端  
> yum install krb5-devel krb5-workstation -y

3 将 krb5.conf 覆盖到 /etc/krb5.conf  

4 最后启动消费者

```
java -cp ./kafkaDemoAcl-1.0.jar  com.xunce.demo.KafkaConsumerDemo ./acl.properties
```

### 5 其他注意事项
需要在 system property 中设置 `java.security.auth.login.config` 选项  

启动时设置  
```
-Djava.security.auth.login.config=/usr/local/kafka/config/kafka_client_jaas.conf
```
也可以在代码里设置
  
```
System.setProperty("java.security.auth.login.config", "/usr/local/kafka/config/kafka_client_jaas.conf");
```

