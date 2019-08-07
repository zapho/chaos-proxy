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

import javax.ejb.Singleton;

@Singleton
public class ReverseProxyConfigurationService {

    private ReverseProxyConfiguration configuration = new ReverseProxyConfiguration();

    public ReverseProxyConfiguration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(ReverseProxyConfiguration configuration) {
        this.configuration = configuration;
    }
}
