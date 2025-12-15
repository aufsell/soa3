package org.lovesoa.calledweb.web;

import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.json.JsonObjectBuilder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.lovesoa.calledejb.service.api.AuthServiceRemote;
import org.lovesoa.calledejb.service.api.HelloServiceRemote;

import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
public class HealthResource {

    @PersistenceContext(unitName = "calledPU")
    private EntityManager em;

    private HelloServiceRemote testService;


    public HealthResource() {
        try {
            Properties props = new Properties();
            Context.URL_PKG_PREFIXES
            props.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.enterprise.naming.SerialInitContextFactory");
            props.put(Context.PROVIDER_URL, "http-remoting://payara-2:3700");


            Context ctx = new InitialContext(props);
            testService = (HelloServiceRemote) ctx.lookup(
                    "ejb:/called-ejb/HelloServiceBean!org.lovesoa.calledejb.service.api.HelloServiceRemote"
            );

        } catch (Exception e) {
            throw new RuntimeException("Cannot lookup remote AuthService", e);
        }
    }

//    @EJB(lookup="corbname:iiop:payara-2:3700#" +"java:global/called-ejb/HelloServiceBean!org.lovesoa.calledejb.service.api.HelloServiceRemote")

    @GET
    public Response health() {
        JsonObjectBuilder result = Json.createObjectBuilder();

        boolean dbOk = checkDb();
        boolean ejbOk = checkEjb();

        boolean up = dbOk && ejbOk;

        result.add("status", up ? "UP" : "DOWN");
        result.add("database", dbOk ? "UP" : "DOWN");
        result.add("ejb", ejbOk ? "UP" : "DOWN");

        return up
                ? Response.ok(result.build()).build()
                : Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(result.build()).build();
    }

    private boolean checkDb() {
        try {
            em.createNativeQuery("SELECT 1").getSingleResult();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private boolean checkEjb() {
        try {
            testService.healthCheck();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
