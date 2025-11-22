package org.lovesoa.calledadapter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/adapter")
public class AdapterController {

    private final PayaraClient payaraClient;

    public AdapterController(PayaraClient payaraClient) {
        this.payaraClient = payaraClient;
    }

    @GetMapping("/test")
    public String test() {
        return "response from called-adapter (stub)";
    }

    @GetMapping("/ping-payara")
    public String pingPayara() {
        return "adapter -> " + payaraClient.pingPayara();
    }
}
