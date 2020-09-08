# kafka 支持 kerberos 认证


### 1 开发 - kafka 支持 kerberos 修改点

**Java kafka**
（1）安装kerberos客户端，然后将提供的 `krb5.conf` 覆盖到 /etc/krb5.conf  
>  yum install krb5-devel krb5-workstation -y 

（2）创建kafka producer（或 consumer)时，增加 Properties 选项  

```
## 下面是kafka SASL kerberos 认证新增的配置
security.protocol = SASL_PLAINTEXT
sasl.kerberos.service.name = kafka
sasl.mechanism = GSSAPI
```

(3) System Property 增加 配置 `java.security.auth.login.config` 选项   
可在启动时设置  
> -Djava.security.auth.login.config=/usr/local/kafka/config/kafka_client_jaas.conf

也可以在代码里面设置  
> System.setProperty("java.security.auth.login.config", "/usr/local/kafka/config/kafka_client_jaas.conf");
 
**C++ kafka**  
 目前 C++ 主要使用的是 `librdkafka` 库 (编译时需支持 `SASL GSSAPI`)
（1） 安装 SASL 认证客户端  
Debian/Ubuntu:  
> sudo apt-get install libsasl2-modules-gssapi-mit  

CentOS/Redhat:
> sudo yum install cyrus-sasl-gssapi

(2) 根据是否启动 kerberos 认证, 调用 `rd_kafka_conf_set()` 设置下列选项
```
        <!-- kafka是否启用kerberos认证: 0不启用，1启用(默认不启动) -->
        <argument name="kerberos.enable">0</argument>
        <argument name="security.protocol">SASL_PLAINTEXT</argument>
        <argument name="sasl.kerberos.service.name">kafka</argument>
        <argument name="sasl.kerberos.keytab">/usr/local/kafka/xunce.keytab</argument>
        <argument name="sasl.kerberos.principal">zookeeper/slave01@XUNCE.COM</argument>
        <argument name="sasl.mechanism">GSSAPI</argument>
```


### 2 集成打包
下面以 redhat 7.5 操作系统为例: 
第三方 依赖库  

- krb5-devel v1.15.1
- krb5-workstation v1.15.1
- cyrus-sasl  v2.1.26
- cyrus-sasl-devel  v2.1.26
- cyrus-sasl-gssapi  v2.1.26
- librdkafka [v1.0.0](https://github.com/edenhill/librdkafka)  

注意编译 librdkafka 前需要先安装 `cyrus-sasl-devel`



### 3 部署注意事项

（1）szshmds 沪深行情程序 (配置修改)
根据客户提供的kafka kero 认证文件，修改 `shmds.xml` `szmds.xml` 文件中对应的配置项  

```
        <!-- kafka是否启用kerberos认证: 0不启用，1启用(默认不启动) -->
        <argument name="kerberos.enable">0</argument>
        <argument name="security.protocol">SASL_PLAINTEXT</argument>
        <argument name="sasl.kerberos.service.name">kafka</argument>
        <argument name="sasl.kerberos.keytab">/usr/local/kafka/xunce.keytab</argument>
        <argument name="sasl.kerberos.principal">zookeeper/slave01@XUNCE.COM</argument>
        <argument name="sasl.mechanism">GSSAPI</argument>
```

注意：如果客户配置文件中没有 `sasl.mechanism` 这一项，需要把 xml 文件中的 `sasl.mechanism` 配置的默认值清空。  


（2）Java 程序修改对应的 properties 配置文件  
  `待补充`