package org.lovesoa.calledweb.web.RemoteClient;

import fish.payara.ejb.http.client.RemoteEJBContextFactory;
import org.lovesoa.calledejb.service.api.HelloServiceRemote;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class RemoteHealthServiceClient {

    private final String ejbHost;

    public RemoteHealthServiceClient(String ejbHost) {
        this.ejbHost = ejbHost;
    }

    protected HelloServiceRemote lookupRemoteBean() throws NamingException {
        Properties props = new Properties();

        props.put(Context.INITIAL_CONTEXT_FACTORY, RemoteEJBContextFactory.class.getName());

        props.put(Context.PROVIDER_URL, "http://" + ejbHost + ":8080/ejb-invoker");

        Context ctx = new InitialContext(props);

        String jndiName = "java:global/called-ejb/HelloServiceBean!org.lovesoa.calledejb.service.api.HelloServiceRemote";

        return (HelloServiceRemote) ctx.lookup(jndiName);
    }

    public void healthCheck() throws NamingException {
        HelloServiceRemote remote = lookupRemoteBean();
        remote.healthCheck();
    }
}
