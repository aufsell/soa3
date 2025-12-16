package org.lovesoa.calledejb.service.api;

import javax.ejb.Remote;

@Remote
public interface HelloServiceRemote {
    default void healthCheck() {};
}
