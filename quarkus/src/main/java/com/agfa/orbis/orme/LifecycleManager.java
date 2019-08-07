/*
 *                  C O P Y R I G H T  (c) 2014
 *     A G F A   H E A L T H C A R E   C O R P O R A T I O N
 *                     All Rights Reserved
 *
 *
 *         THIS IS UNPUBLISHED PROPRIETARY SOURCE CODE OF
 *                       AGFA CORPORATION
 *        The copyright notice above does not evidence any
 *       actual or intended publication of such source code.
 */
package com.agfa.orbis.orme;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.littleshoot.proxy.impl.ThreadPoolConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.bind.JsonbBuilder;
import java.io.FileInputStream;
import java.io.InputStream;

@ApplicationScoped
public class LifecycleManager {

    private static final Logger LOG = LoggerFactory.getLogger(LifecycleManager.class);

    HttpProxyServer server;

    @ConfigProperty(name = "proxy.port")
    int proxyPort;

    @ConfigProperty(name = "proxy.maxConcurrency")
    int proxyMaxConcurrency;

    @Inject
    HttpChaosInterceptor interceptor;

    @Inject
    ProxyConfigurationService configurationService;

    void onStart(@Observes StartupEvent ev) {
        if (proxyPort > 0) {
            ThreadPoolConfiguration threadPoolConf = new ThreadPoolConfiguration()
                    .withClientToProxyWorkerThreads(proxyMaxConcurrency)
                    .withAcceptorThreads(4)
                    .withProxyToServerWorkerThreads(proxyMaxConcurrency);
            server = DefaultHttpProxyServer.bootstrap()
                    .withPort(proxyPort)
                    .withFiltersSource(interceptor)
                    .withThreadPoolConfiguration(threadPoolConf)
                    .start();

            LOG.info("Bootstrap proxy on {}", server.getListenAddress().toString());

            loadConfigurationFromFile();

            LOG.info("Chaos Proxy started. Read/Write/Delete configuration via GET|PUT|DELETE /resources/chaos/conf.");

        } else {
            LOG.error("Unable to start proxy, invalid port: {}", proxyPort);
        }
    }

    private void loadConfigurationFromFile() {
        String configFile = System.getProperty("configFile");
        if (configFile != null) {
            try {
                ProxyConfiguration proxyConfiguration = JsonbBuilder.create().fromJson(new FileInputStream(configFile), ProxyConfiguration.class);
                configurationService.setConfiguration(proxyConfiguration);
                LOG.info("Loaded config file from {}", configFile);
            } catch (Exception e) {
                LOG.warn("Unable to load configuration file {}, error: {}", configFile, e.getMessage());
            }
        }
    }

    void onStop(@Observes ShutdownEvent ev) {
        if (server != null) {
            server.abort();
        }
    }

}
