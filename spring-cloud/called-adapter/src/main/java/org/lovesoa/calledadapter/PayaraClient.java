package org.lovesoa.calledadapter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class PayaraClient {

    private final DiscoveryClient consulDiscoveryClient;
    private final RestTemplate restTemplate;

    public PayaraClient(DiscoveryClient consulDiscoveryClient,
                        RestTemplate restTemplate) {
        this.consulDiscoveryClient = consulDiscoveryClient;
        this.restTemplate = restTemplate;
    }

    public String pingPayara() {
        String serviceName = "called-service";

        List<ServiceInstance> instances = consulDiscoveryClient.getInstances(serviceName);
        if (instances == null || instances.isEmpty()) {
            throw new IllegalStateException("No instances of " + serviceName + " found in Consul");
        }

        ServiceInstance instance = instances.get(
                ThreadLocalRandom.current().nextInt(instances.size())
        );

        String baseUrl = instance.getUri().toString();
        String url = baseUrl + "/called/api/ping";

        return restTemplate.getForObject(url, String.class);
    }
}
