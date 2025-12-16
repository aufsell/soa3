package org.lovesoa.calledweb.web.RemoteClient;

import fish.payara.ejb.http.client.RemoteEJBContextFactory;
import org.lovesoa.calledejb.service.api.HelloServiceRemote;
import org.lovesoa.calledejb.service.api.PingServiceRemote;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class RemotePingServiceClient {
    private final String ejbHost;

    public RemotePingServiceClient(String ejbHost) {
        this.ejbHost = ejbHost;
    }

    protected PingServiceRemote lookupRemoteBean() throws NamingException {
        Properties props = new Properties();

        props.put(Context.INITIAL_CONTEXT_FACTORY, RemoteEJBContextFactory.class.getName());

        props.put(Context.PROVIDER_URL, "http://" + ejbHost + ":8080/ejb-invoker");

        Context ctx = new InitialContext(props);

        String jndiName = "java:global/called-ejb/PingServiceBean!org.lovesoa.calledejb.service.api.PingServiceRemote";

        return (PingServiceRemote) ctx.lookup(jndiName);
    }

    public String ping() throws NamingException {
        PingServiceRemote remote = lookupRemoteBean();
        return remote.ping();
    }
}
