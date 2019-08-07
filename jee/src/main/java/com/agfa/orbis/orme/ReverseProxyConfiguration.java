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

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class ReverseProxyConfiguration {
    private boolean blockOutgoingRequest = false;
    private int latencyInMs = -1;

    public boolean isBlockOutgoingRequest() {
        return blockOutgoingRequest;
    }

    public void setBlockOutgoingRequest(boolean blockOutgoingRequest) {
        this.blockOutgoingRequest = blockOutgoingRequest;
    }

    public long getLatencyInMs() {
        return latencyInMs;
    }

    public void setLatencyInMs(int latencyInMs) {
        this.latencyInMs = latencyInMs;
    }

    public JsonObject toJson() {
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("blockOutgoingRequest", blockOutgoingRequest);
        job.add("latencyInMs", latencyInMs);
        return job.build();
    }

    public static ReverseProxyConfiguration fromJson(JsonObject jo) {
        if (jo == null) {
            throw new IllegalArgumentException();
        }
        boolean blockOutgoingRequest = jo.getBoolean("blockOutgoingRequest", false);
        int latencyInMs = jo.getInt("latencyInMs", -1);
        ReverseProxyConfiguration conf = new ReverseProxyConfiguration();
        conf.setBlockOutgoingRequest(blockOutgoingRequest);
        conf.setLatencyInMs(latencyInMs);
        return conf;
    }

}
