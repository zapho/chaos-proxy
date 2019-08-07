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

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbTransient;
import java.util.Objects;

public class ProxyConfiguration {

    private String host;
    private boolean isBlockOutgoingRequest = false;
    private int latencyInMs = -1;

    @JsonbProperty("isBlockOutgoingRequest")
    public boolean isBlockOutgoingRequest() {
        return isBlockOutgoingRequest;
    }

    public void setBlockOutgoingRequest(boolean blockOutgoingRequest) {
        this.isBlockOutgoingRequest = blockOutgoingRequest;
    }

    @JsonbProperty("latencyInMs")
    public long getLatencyInMs() {
        return latencyInMs;
    }

    public void setLatencyInMs(int latencyInMs) {
        this.latencyInMs = latencyInMs;
    }

    @JsonbProperty("host")
    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = Objects.requireNonNull(host);
    }

    @JsonbTransient
    public boolean isValid() {
        return host != null && !host.isEmpty();
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        ProxyConfiguration that = (ProxyConfiguration) o;
        return host.equals(that.host);
    }

    @Override public int hashCode() {
        return Objects.hash(host);
    }
}
