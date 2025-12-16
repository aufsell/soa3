package org.lovesoa.calledweb.web.consul;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Singleton
@Startup
public class ConsulRegistrar {

    private static final String CONSUL_HOST =
            System.getenv().getOrDefault("CONSUL_HOST", "consul");
    private static final int CONSUL_PORT =
            Integer.parseInt(System.getenv().getOrDefault("CONSUL_PORT", "8500"));
    private static final String SERVICE_ID =
            System.getenv().getOrDefault("SERVICE_ID", UUID.randomUUID().toString());
    private static final String SERVICE_NAME =
            System.getenv().getOrDefault("SERVICE_NAME", "called-service");
    private static final String SERVICE_ADDRESS =
            System.getenv().getOrDefault("SERVICE_ADDRESS", "payara");
    private static final int SERVICE_PORT =
            Integer.parseInt(System.getenv().getOrDefault("SERVICE_PORT", "8080"));


    @PostConstruct
    public void register() {
        try {
            String consulUrl = "http://" + CONSUL_HOST + ":" + CONSUL_PORT + "/v1/agent/service/register";

            String json = String.format(
                    "{\n" +
                            "  \"ID\": \"%s\",\n" +
                            "  \"Name\": \"%s\",\n" +
                            "  \"Address\": \"%s\",\n" +
                            "  \"Port\": %d,\n" +
                            "  \"Check\": {\n" +
                            "    \"HTTP\": \"http://%s:%d/called/api/health\",\n" +
                            "    \"Interval\": \"10s\",\n" +
                            "    \"Timeout\": \"2s\"\n" +
                            "  }\n" +
                            "}",
                    SERVICE_ID,
                    SERVICE_NAME,
                    SERVICE_ADDRESS,
                    SERVICE_PORT,
                    SERVICE_ADDRESS,
                    SERVICE_PORT
            );


            URL url = new URL(consulUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");
            conn.setDoOutput(true);
            conn.setRequestProperty("Content-Type", "application/json");

            try (OutputStream os = conn.getOutputStream()) {
                os.write(json.getBytes(StandardCharsets.UTF_8));
            }

            int code = conn.getResponseCode();
            if (code >= 200 && code < 300) {
                System.out.println("[ConsulRegistrar] Service registered in Consul with id=" + SERVICE_ID);
            } else {
                System.err.println("[ConsulRegistrar] Failed to register service in Consul. HTTP " + code);
            }

        } catch (Exception e) {
            System.err.println("[ConsulRegistrar] Error registering service in Consul: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void deregister() {
        try {
            String consulUrl = "http://" + CONSUL_HOST + ":" + CONSUL_PORT
                    + "/v1/agent/service/deregister/" + SERVICE_ID;

            URL url = new URL(consulUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("PUT");

            int code = conn.getResponseCode();
            if (code >= 200 && code < 300) {
                System.out.println("[ConsulRegistrar] Service deregistered from Consul: " + SERVICE_ID);
            } else {
                System.err.println("[ConsulRegistrar] Failed to deregister service from Consul. HTTP " + code);
            }

        } catch (Exception e) {
            System.err.println("[ConsulRegistrar] Error deregistering service from Consul: " + e.getMessage());
            e.printStackTrace();
        }
    }

}

