package io.quarkiverse.it.cxf;
import io.quarkus.arc.Unremovable;

import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.wss4j.common.ConfigurationConstants;
import org.apache.wss4j.dom.WSConstants;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import java.util.*;

public class GreetingProducer {
    @Inject
    @ConfigProperty(name = "mask.sensitiveElementNames")
    List<String> sensitiveElementNames;

    @Inject
    UsernameTokenPasswordServerCallback usernameTokenPasswordServerCallback;

    @Produces
    @Unremovable
    @ApplicationScoped
    public WSS4JInInterceptor getWSS4JInInterceptor() {
        Map<String,Object> inProps = new HashMap<>();
        inProps.put(ConfigurationConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
        inProps.put(ConfigurationConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
//        inProps.put(ConfigurationConstants.PW_CALLBACK_CLASS, UsernameTokenPasswordServerCallback.class.getName());
        inProps.put(ConfigurationConstants.PW_CALLBACK_REF, usernameTokenPasswordServerCallback);
        return new WSS4JInInterceptor(inProps);
    }
}