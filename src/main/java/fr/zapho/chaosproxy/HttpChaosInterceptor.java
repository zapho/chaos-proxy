package fr.zapho.chaosproxy;

import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.LastHttpContent;
import org.apache.commons.lang3.RandomUtils;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class HttpChaosInterceptor extends HttpFiltersSourceAdapter {

    private final static Logger LOG = LoggerFactory.getLogger(HttpChaosInterceptor.class);

    @Inject
    ProxyConfigurationService configurationService;

    @Inject
    LoggerService loggerService;

    @Override public HttpFilters filterRequest(HttpRequest originalRequest) {
        return new HttpFiltersAdapter(originalRequest) {

            @Override
            public HttpResponse clientToProxyRequest(HttpObject httpObject) {
                String uri = originalRequest.uri();
                loggerService.log(LogLevel.TRACE, LOG, uri);
                return configurationService.getConfiguration(uri)
                        .map(conf -> unleashHttpChaos(uri, httpObject, conf)) // URI & conf matching > introduce issues in HTTP request/response
                        .orElse(null); // no conf matching URI > do not mingle with HTTP
            }
        };
    }

    private HttpResponse unleashHttpChaos(String uri, HttpObject httpObject, ProxyConfiguration conf) {

        if (httpObject instanceof LastHttpContent) {
            return null;
        }
        double badLuck = RandomUtils.nextDouble(0.0, 1.0);
        if (badLuck < conf.getErrorRate()) {
            if (conf.isBlockingOutgoingRequest()) {

                loggerService.log(LogLevel.INFO, LOG, ">> Blocking call to {}", uri);
                DefaultHttpResponse err500 = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.INTERNAL_SERVER_ERROR);
                err500.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.CLOSE);
                return err500;
            } else if (conf.getLatencyInMs() > 0) {
                try {
                    loggerService.log(LogLevel.INFO, LOG, ">> Latency of {} introduced for call to {}", conf.getLatencyInMs(), uri);
                    Thread.sleep(conf.getLatencyInMs());
                    return null;
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return null;
    }
}
