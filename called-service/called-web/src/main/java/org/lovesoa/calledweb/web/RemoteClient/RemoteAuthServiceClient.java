package org.lovesoa.calledweb.web.RemoteClient;

import fish.payara.ejb.http.client.RemoteEJBContextFactory;
import org.lovesoa.calledejb.dtos.AuthResponse;
import org.lovesoa.calledejb.dtos.LoginRequest;
import org.lovesoa.calledejb.dtos.RegisterRequest;
import org.lovesoa.calledejb.service.api.AuthServiceRemote;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class RemoteAuthServiceClient {
    private final String ejbHost;

    public RemoteAuthServiceClient(String ejbHost) {
        this.ejbHost = ejbHost;
    }

    protected AuthServiceRemote lookupRemoteBean() throws NamingException {
        Properties props = new Properties();

        props.put(Context.INITIAL_CONTEXT_FACTORY, RemoteEJBContextFactory.class.getName());

        props.put(Context.PROVIDER_URL, "http://" + ejbHost + ":8080/ejb-invoker");

        Context ctx = new InitialContext(props);

        String jndiName = "java:global/called-ejb/AuthServiceBean!org.lovesoa.calledejb.service.api.AuthServiceRemote";

        return (AuthServiceRemote) ctx.lookup(jndiName);
    }

    public AuthResponse register(RegisterRequest request) throws NamingException {
        AuthServiceRemote remote = lookupRemoteBean();
         return remote.register(request);
    }
    public AuthResponse login(LoginRequest request) throws NamingException {
        AuthServiceRemote remote = lookupRemoteBean();
        return remote.login(request);
    }
}
