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
package fr.zapho.chaosproxy;

import javax.json.bind.annotation.JsonbProperty;
import javax.json.bind.annotation.JsonbPropertyOrder;
import javax.json.bind.annotation.JsonbTransient;
import javax.json.bind.config.PropertyOrderStrategy;
import java.util.Objects;

@JsonbPropertyOrder(PropertyOrderStrategy.LEXICOGRAPHICAL)
public class ProxyConfiguration {
    @JsonbProperty("host")
    private String host;
    @JsonbProperty("blockOutgoingRequest")
    private Boolean blockingOutgoingRequest = false;
    @JsonbProperty("latencyInMs")
    private int latencyInMs = -1;
    @JsonbProperty("errorRate")
    private double errorRate = 1;

    public boolean isBlockingOutgoingRequest() {
        return this.blockingOutgoingRequest;
    }

    public void setBlockingOutgoingRequest(boolean blockingOutgoingRequest) {
        this.blockingOutgoingRequest = blockingOutgoingRequest;
    }

    public long getLatencyInMs() {
        return latencyInMs;
    }

    public void setLatencyInMs(int latencyInMs) {
        this.latencyInMs = latencyInMs;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = Objects.requireNonNull(host);
    }

    public double getErrorRate() {
        return errorRate;
    }

    public void setErrorRate(double errorRate) {
        if (errorRate > 0 && errorRate <= 1) {
            this.errorRate = errorRate;
        }
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

    @Override public String toString() {
        return "ProxyConfiguration{" +
                "host='" + host + '\'' +
                ", blockOutgoingRequest=" + blockingOutgoingRequest +
                ", latencyInMs=" + latencyInMs +
                ", errorRate=" + errorRate +
                '}';
    }
}
