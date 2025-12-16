package org.lovesoa.calledejb.service.impl;

import javax.ejb.Stateless;
import org.lovesoa.calledejb.service.api.PingServiceRemote;

@Stateless
public class PingServiceBean implements PingServiceRemote {
    @Override
    public String ping() {
        return "ejb pong";
    }
}
