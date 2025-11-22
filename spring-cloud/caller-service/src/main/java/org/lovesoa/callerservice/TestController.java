package org.lovesoa.callerservice;

import org.lovesoa.callerservice.dtos.RedistributeRewardsResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.PathParam;

@RestController
@RequestMapping("/caller")
public class TestController {

    private final CalledAdapterClient adapterClient;

    public TestController(CalledAdapterClient adapterClient) {
        this.adapterClient = adapterClient;
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong from caller-service";
    }

    @GetMapping("/call-adapter")
    public String callAdapter() {
        return "caller -> " + adapterClient.callAdapter();
    }

    @GetMapping("/ping-payara-via-cloud")
    public String pingPayaraViaCloud() {
        return "caller -> " + adapterClient.pingPayaraViaAdapter();
    }



}
