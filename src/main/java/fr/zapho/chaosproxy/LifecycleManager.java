package fr.zapho.chaosproxy;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.HttpProxyServerBootstrap;
import org.littleshoot.proxy.ProxyAuthenticator;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.littleshoot.proxy.impl.ThreadPoolConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.json.bind.JsonbBuilder;
import java.io.FileInputStream;
import java.util.Optional;

@ApplicationScoped
public class LifecycleManager {

    private static final Logger LOG = LoggerFactory.getLogger(LifecycleManager.class);

    HttpProxyServer server;

    @ConfigProperty(name = "proxy.port")
    int proxyPort;

    @ConfigProperty(name = "proxy.maxConcurrency")
    int proxyMaxConcurrency;

    @ConfigProperty(name = "proxy.username")
    Optional<String> usernameConf;

    @ConfigProperty(name = "proxy.password")
    Optional<String> passwordConf;

    @Inject
    HttpChaosInterceptor interceptor;

    @Inject
    ProxyConfigurationService configurationService;

    @Inject
    LoggerService loggerService;

    void onStart(@Observes StartupEvent ev) {
        if (proxyPort > 0) {
            ThreadPoolConfiguration threadPoolConf = new ThreadPoolConfiguration()
                    .withClientToProxyWorkerThreads(proxyMaxConcurrency)
                    .withAcceptorThreads(4)
                    .withProxyToServerWorkerThreads(proxyMaxConcurrency);
            HttpProxyServerBootstrap bootstrap = DefaultHttpProxyServer.bootstrap()
                    .withPort(proxyPort)
                    .withAllowLocalOnly(false)
                    .withFiltersSource(interceptor)
                    .withThreadPoolConfiguration(threadPoolConf);

            if (usernameConf.isPresent() && passwordConf.isPresent()) {
                loggerService.log(LogLevel.INFO, LOG, "Authentication activated");
                final String u = usernameConf.get();
                final String p = passwordConf.get();
                if (!u.isEmpty() && !p.isEmpty()) {
                    ProxyAuthenticator auth = new ProxyAuthenticator() {
                        @Override
                        public boolean authenticate(String username, String password) {
                            return u.equals(username) && p.equals(password);
                        }

                        @Override
                        public String getRealm() {
                            return null;
                        }
                    };
                    bootstrap = bootstrap.withProxyAuthenticator(auth);
                }
            }

            server = bootstrap.start();

            loggerService.log(LogLevel.INFO, LOG, "Bootstrap proxy on {}", server.getListenAddress().toString());

            loadConfigurationFromFile();

            loggerService.log(LogLevel.INFO, LOG, "Chaos Proxy started. Read/Write/Delete configuration via GET|PUT|DELETE /resources/chaos/conf.");

        } else {
            loggerService.log(LogLevel.ERROR, LOG, "Unable to start proxy, invalid port: {}", proxyPort);
        }
    }

    private void loadConfigurationFromFile() {
        String configFile = System.getProperty("configFile");
        if (configFile != null) {
            try {
                ProxyConfiguration proxyConfiguration = JsonbBuilder.create().fromJson(new FileInputStream(configFile), ProxyConfiguration.class);
                if (proxyConfiguration.isValid()) {
                    loggerService.log(LogLevel.INFO, LOG, "Load config file from {}", configFile);
                    configurationService.setConfiguration(proxyConfiguration);
                } else {
                    loggerService.log(LogLevel.WARN, LOG, "Unable to load configuration file {}, error: invalid configuration", configFile);
                }
            } catch (Exception e) {
                loggerService.log(LogLevel.WARN, LOG, "Unable to load configuration file {}, error: {}", configFile, e.getMessage());
            }
        }
    }

    void onStop(@Observes ShutdownEvent ev) {
        if (server != null) {
            server.abort();
        }
    }

}
