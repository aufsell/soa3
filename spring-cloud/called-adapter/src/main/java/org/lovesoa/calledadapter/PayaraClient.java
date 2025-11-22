package org.lovesoa.calledadapter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PayaraClient {

    private final RestTemplate restTemplate;
    private final String gatewayBaseUrl;

    public PayaraClient(
            RestTemplate restTemplate,
            @Value("${gateway.url:http://api-gateway:8080}") String gatewayBaseUrl
    ) {
        this.restTemplate = restTemplate;
        this.gatewayBaseUrl = gatewayBaseUrl;
    }

    public String pingPayara() {
        String url = gatewayBaseUrl + "/called/api/ping";
        return restTemplate.getForObject(url, String.class);
    }
}
