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
