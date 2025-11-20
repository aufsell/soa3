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
import org.lovesoa.calledejb.service.api.HelloServiceRemote;

@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
public class HealthResource {

    @PersistenceContext(unitName = "calledPU")
    private EntityManager em;

    @EJB
    private HelloServiceRemote testService;

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
