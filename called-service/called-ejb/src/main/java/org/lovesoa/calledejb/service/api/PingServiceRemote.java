package org.lovesoa.calledejb.service.api;

import jakarta.ejb.Remote;

@Remote
public interface PingServiceRemote {
    String ping();
}
