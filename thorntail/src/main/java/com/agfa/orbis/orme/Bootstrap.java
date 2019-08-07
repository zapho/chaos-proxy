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

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

@ApplicationScoped
public class Bootstrap {

    HttpProxyServer server;

    @Inject
    OrmeInterceptor interceptor;

    public void onStartup(@Observes @Initialized(ApplicationScoped.class) Object init) {

        server = DefaultHttpProxyServer.bootstrap()
                .withPort(8888)
                .withFiltersSource(interceptor)
                .start();

        System.out.println("Bootstrap reverse proxy on " + server.getListenAddress().toString());
    }

    public void onShutdown(@Observes @Destroyed(ApplicationScoped.class) Object init) {
        if (server != null) {
            server.abort();
        }
    }

}
