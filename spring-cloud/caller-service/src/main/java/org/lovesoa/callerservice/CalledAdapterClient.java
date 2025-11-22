package org.lovesoa.callerservice;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CalledAdapterClient {

    private final RestTemplate restTemplate;

    public CalledAdapterClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String callAdapter() {
        String resp = restTemplate.getForObject(
                "http://called-adapter/adapter/test",
                String.class
        );
        return resp != null ? resp : "no response";
    }

    public String pingPayaraViaAdapter() {
        String resp = restTemplate.getForObject(
                "http://called-adapter/adapter/ping-payara",
                String.class
        );
        return resp != null ? resp : "no response from adapter";
    }
}
