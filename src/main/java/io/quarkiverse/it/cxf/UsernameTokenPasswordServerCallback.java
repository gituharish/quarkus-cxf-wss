package io.quarkiverse.it.cxf;


import io.quarkiverse.it.cxf.properties.AppProperties;
import io.quarkus.arc.Unremovable;
import io.quarkus.runtime.Startup;
import io.quarkus.runtime.annotations.QuarkusMain;
import io.quarkus.runtime.annotations.RegisterForReflection;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import org.apache.wss4j.common.ext.WSPasswordCallback;
@Singleton
@Startup
//@RegisterForReflection
public class UsernameTokenPasswordServerCallback implements CallbackHandler {

    @Inject
    AppProperties appProperties;
    public Map<String, String> passwords = new HashMap();

    public UsernameTokenPasswordServerCallback() {
        this.passwords.put(appProperties.username(), appProperties.password());
//        this.passwords.put("testuser", "testpwd");
    }

    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
        Callback[] var2 = callbacks;
        int var3 = callbacks.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            Callback callback = var2[var4];
            WSPasswordCallback pc = (WSPasswordCallback)callback;
            String pass = (String)this.passwords.get(pc.getIdentifier());
            if (pass != null) {
                pc.setPassword(pass);
                return;
            }
        }

    }
}
