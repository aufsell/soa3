package org.lovesoa.calledejb.service.impl;

import org.lovesoa.calledejb.service.api.HelloServiceRemote;

import javax.ejb.Stateless;

@Stateless
public class HelloServiceBean implements HelloServiceRemote {
    @Override
    public void healthCheck() {
        System.out.println("HealtCheck на второй паяре");
        // No operation
    }
}
