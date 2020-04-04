The chaos proxy is a lightweight java-based tool to inject HTTP/network errors between a client and a remote host.

[![Build Status](https://travis-ci.org/zapho/chaos-proxy.svg?branch=master)](https://travis-ci.org/zapho/chaos-proxy)

# Usage

- Start the chaos proxy
   ```bash
   java -jar chaos-proxy-runner.jar
   ```
- Configure your client to use the proxy
  - Any JVM-based client
     ```bash
     -DproxySet=true -Dhttp.proxyHost=127.0.0.1 -Dhttp.proxyPort=8888 -Dhttps.proxyHost=127.0.0.1 -Dhttps.proxyPort=8888
     ```
  - HTTPie
    ```bash
    http --proxy=http:http://localhost:8888 http://somehost.org:80
    ```
   By default, the proxy does not do anything. Let's introduce some chaos now.
- Start messing around with HTTP but pushing configuration into the proxy using your
favorite REST tool
  - Add chaos between your client and the host
    ```bash
    PUT http://localhost:18080/resources/chaos/conf
    {
        "host": "somehost.org",
        "errorRate": 0.5,
        "blockOutgoingRequest": true
    }
    ```
    This configuration is blocking 1 request out of 2 by returning a 500 response error. Configurations are described below.
  - Modify an existing configuration
    ```bash
    PUT http://localhost:18080/resources/chaos/conf
    {
        "host": "somehost.org",
        "errorRate": 0.75,
        "latencyInMs": 5000
    }
    ```
    This configuration is introducing a 5s latency in 75% of requests.
  - Check the existing configuration
    ```bash
    GET http://localhost:18080/resources/chaos/conf
    ```
  - Remove all configurations
    ```bash
    DELETE http://localhost:18080/resources/chaos/conf
    ```
  - Have look at what's happening inside the proxy
    ```bash
    GET http://localhost:18080/resources/chaos/logs
    ```
    ```bash
    [
      "2019-08-23 15:15:51:1551 - TRACE - http://somehost.org:8021/service/",
      ...
      "2019-08-23 15:15:46:1546 - INFO - Proxy configuration added/changed: ProxyConfiguration{host='somehost.org', blockOutgoingRequest=true, errorRate=1.0}",
      "2019-08-23 15:16:43:1643 - INFO - >> Blocking call to http://somehost.org:8021/service/"
      ...
    ]
    ```
    Same thing with less details
    ```bash
    GET http://localhost:18080/resources/chaos/logs?lvl=info
    ```
    By default, proxy events are logged at the INFO level. To see all requests going through the proxy level must be set to TRACE:
    ```bash
    java -Dquarkus.log.category.\"fr.zapho.chaosproxy\".level=TRACE -jar chaos-proxy-runner.jar
    ``` 
 
  - Optionally, a configuration can be specified at startup:
    ```bash
    java -DconfigFile=C:/tmp/chaos.conf -jar chaos-proxy-runner.jar
    ```
    where the content of the config file is identical to the configurations shown above.
    
# Configuration

Key | Value | Required | Default
--- | --- | --- | ---
host | The target host name, or part of the hostname, regardless of the port: e.g. `somehost.org` will match requests targetted to www.somehost.org, somehost.org:8080, secure.somehost.org:433, etc. If no host is configured, the proxy will do nothing. | true | -
errorRate | The proportion of HTTP requests sent out to the host that will be tampered with (i.e. one of the actions below will be applied). A rate of 1 meaning all requests will be targetted whereas a rate of 0 means no requests wll. | false | 1
blockOutgoingRequest | One of the actions that may be applied to a targeted host. If applied, this action will immediately block the request from reaching the host and return a 500 response error.<br><br> **If this action is activated, no other actions will be applied**. | false | false
latencyInMs | One of the actions that may be applied to a targeted host. If applied, this action will add latency to the request before reaching the host. A value of -1 means no latency is applied. | false | -1

# Acknowledgements

All the heavy lifting is done thanks to [LittleProxy](https://github.com/adamfisk/LittleProxy).

Project versioning is handle by [jgitver](https://jgitver.github.io/)
