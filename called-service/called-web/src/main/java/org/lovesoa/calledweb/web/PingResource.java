package org.lovesoa.calledweb.web;

import javax.naming.NamingException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.lovesoa.calledweb.web.RemoteClient.RemotePingServiceClient;


@Path("/ping")
@Produces(MediaType.APPLICATION_JSON)
public class PingResource {

    private final RemotePingServiceClient client =
            new RemotePingServiceClient("payara-2");

    @GET
    public String ping() throws NamingException {
        return client.ping();
}
}
