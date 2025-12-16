package org.lovesoa.calledejb.service.api;

import javax.ejb.Remote;

@Remote
public interface PingServiceRemote {
    String ping();
}
