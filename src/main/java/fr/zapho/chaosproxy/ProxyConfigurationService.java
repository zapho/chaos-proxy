
package fr.zapho.chaosproxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@ApplicationScoped
public class ProxyConfigurationService {

    private final static Logger LOG = LoggerFactory.getLogger(ProxyConfigurationService.class);

    @Inject
    LoggerService loggerService;

    List<ProxyConfiguration> conf = new CopyOnWriteArrayList<>();

    public Optional<ProxyConfiguration> getConfiguration(String host) {
        if (host == null) {
            return Optional.empty();
        }
        return conf
                .stream()
                .filter(conf -> host.toLowerCase().contains(conf.getHost().toLowerCase()))
                .findFirst();
    }

    public List<ProxyConfiguration> getConfigurations() {
        return Collections.unmodifiableList(conf);
    }

    public void setConfiguration(ProxyConfiguration configuration) {
        if (configuration == null) {
            return;
        }
        if (conf.contains(configuration)) {
            conf.remove(configuration);
        }
        conf.add(configuration);
        if (LOG.isInfoEnabled()) {
            loggerService.log(LogLevel.INFO, LOG, "Proxy configuration added/changed: {}", configuration.toString());
        }
    }

    public void removeConfiguration() {
        if (LOG.isInfoEnabled()) {
            loggerService.log(LogLevel.INFO, LOG, "Proxy configuration removed (all entries)");
        }
        conf.clear();
    }
}
