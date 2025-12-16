package org.lovesoa.calledweb.web;



import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.lovesoa.calledweb.web.RemoteClient.RemoteHealthServiceClient;


@Path("/health")
@Produces(MediaType.APPLICATION_JSON)
public class HealthResource {

    private final RemoteHealthServiceClient client =
            new RemoteHealthServiceClient("payara-2");

    @PersistenceContext(unitName = "calledPU")
    private EntityManager em;


    @GET
    public Response health() {
        boolean dbOk = checkDb();
        boolean ejbOk = checkEjb();

        boolean up = dbOk && ejbOk;

        JsonObjectBuilder result = Json.createObjectBuilder()
                .add("status", up ? "UP" : "DOWN")
                .add("database", dbOk ? "UP" : "DOWN")
                .add("ejb", ejbOk ? "UP" : "DOWN");

        return up
                ? Response.ok(result.build()).build()
                : Response.status(Response.Status.SERVICE_UNAVAILABLE)
                .entity(result.build())
                .build();
    }

    private boolean checkDb() {
        try {
            em.createNativeQuery("SELECT 1").getSingleResult();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private boolean checkEjb() {
        System.out.println("Попытка удалённого Healcheck");
        try {
            client.healthCheck();
            System.out.println("Пинг успешен");
            return true;
        } catch (Exception e) {
            // Печатаем полное исключение для отладки
            e.printStackTrace(System.out);
            System.out.println("Пинг не удался: " + e.getMessage());
            return false;
        }
    }
}

