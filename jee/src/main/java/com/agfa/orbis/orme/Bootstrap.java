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

import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
public class Bootstrap {

    HttpProxyServer server;

    @Inject
    OrmeInterceptor interceptor;

    @PostConstruct
    public void onStartup() {

        server = DefaultHttpProxyServer.bootstrap()
                .withPort(8888)
                .withFiltersSource(interceptor)
                .start();

        System.out.println("Bootstrap reverse proxy on " + server.getListenAddress().toString());
    }

    @PreDestroy
    public void onShutdown() {
        if (server != null) {
            server.abort();
        }
    }

}
