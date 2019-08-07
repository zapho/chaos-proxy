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

import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class OrmeInterceptor extends HttpFiltersSourceAdapter {

    private

    @Inject
    ReverseProxyConfigurationService configurationService;

    @Override public HttpFilters filterRequest(HttpRequest originalRequest) {
        return new HttpFiltersAdapter(originalRequest) {

            @Override
            public HttpResponse clientToProxyRequest(HttpObject httpObject) {
                if (originalRequest.getUri().contains("bodswv019")) {
                    //System.out.println("!!!!!!!!! bodswv019 >>> " + httpObject.toString());
                    if (configurationService.getConfiguration().isBlockOutgoingRequest()) {
                        System.out.println("xxxxxxxxxxx Blocking call to bodswv019");
                        return new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
                    } else if (configurationService.getConfiguration().getLatencyInMs() > 0) {
                        try {
                            System.out.println("xxxxxxxxxxx Latency to bodswv019 = " + configurationService.getConfiguration().getLatencyInMs());
                            Thread.sleep(configurationService.getConfiguration().getLatencyInMs());
                            return null;
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
                return null;
            }
        };
    }
}
