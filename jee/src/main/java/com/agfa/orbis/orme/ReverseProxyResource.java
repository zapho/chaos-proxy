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

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Stateless
@Path("rp")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ReverseProxyResource {

    @Inject
    ReverseProxyConfigurationService configurationService;

    @Path("conf")
    @GET
    public Response readConfiguration() {
        return Response.ok().entity(configurationService.getConfiguration().toJson()).build();
    }

    @Path("conf")
    @PUT
    public Response writeConfiguration(JsonObject confPayload) {
        ReverseProxyConfiguration newConf = ReverseProxyConfiguration.fromJson(confPayload);
        configurationService.setConfiguration(newConf);
        return Response.ok().build();
    }

}
