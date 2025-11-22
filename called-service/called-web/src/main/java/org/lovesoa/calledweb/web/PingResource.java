package org.lovesoa.calledweb.web;


import jakarta.ejb.EJB;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import org.lovesoa.calledejb.service.api.PingServiceRemote;

@Path("/ping")
@Produces(MediaType.APPLICATION_JSON)
public class PingResource {

    @EJB
    private PingServiceRemote pingService;

    @GET
    public String ping() {
        return pingService.ping();
}
}
