package org.lovesoa.calledejb.service.impl;

import jakarta.ejb.Stateless;
import org.lovesoa.calledejb.service.api.HelloServiceRemote;

@Stateless
public class HelloServiceBean implements HelloServiceRemote {
    @Override
    public void healthCheck() {
        // No operation
    }
}
