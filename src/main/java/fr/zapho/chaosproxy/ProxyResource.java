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

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@ApplicationScoped
@Path("chaos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProxyResource {

    @Inject
    ProxyConfigurationService configurationService;

    @Inject
    LoggerService loggerService;

    @Path("conf")
    @GET
    public Response readConfiguration() {
        List<ProxyConfiguration> configurations = configurationService.getConfigurations();
        return Response.ok(configurations).build();
    }

    @Path("conf")
    @PUT
    public Response writeConfiguration(ProxyConfiguration newConf) {
        if (newConf == null ) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        if (!newConf.isValid()) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        configurationService.setConfiguration(newConf);
        return Response.ok().build();
    }

    @Path("conf")
    @DELETE
    public Response removeConfiguration() {
        configurationService.removeConfiguration();
        return Response.ok().build();
    }

    @Path("logs")
    @GET
    public Response logs(@QueryParam("lvl") String level) {
        return Response.ok(loggerService.getLogs(level)).build();
    }

    @Path("logs")
    @DELETE
    public Response cleanLogs() {
        loggerService.cleanLogs();
        return Response.ok().build();
    }

}
