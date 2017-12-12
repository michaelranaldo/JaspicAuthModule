package uk.co.ranaldo.playground.jaspicauthmodule;

import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.module.ServerAuthModule;

/**
 *
 * @author Michael Ranaldo <michael@ranaldo.co.uk>
 */
public class JaspicAuthModule implements ServerAuthModule {
    
    private CallbackHandler handler;

    /**
     * Initialize our module (constructor).
     * @param requestPolicy
     * @param responsePolicy
     * @param handler
     * @param options
     * @throws AuthException 
     */
    @Override
    public void initialize(MessagePolicy requestPolicy, MessagePolicy responsePolicy, CallbackHandler handler, Map options) throws AuthException {
        System.out.println("Initialize Called.");
        this.handler = handler;
    }

    /**
     * Return the HTTP Servlet Request and Response types.
     * @return [ request.class, response.class ]
     */
    @Override
    public Class[] getSupportedMessageTypes() {
        throw new UnsupportedOperationException("Not supported yet. "
                + "Ironic; he could support other message types, but not himself.");
    }

    /**
     * Main method: In order to pass on the user and role, this example has them as servlet request parameters. 
     * This method extricates the user and role from the servlet parameters to authenticate them via authenticateUser.
     * @param messageInfo
     * @param clientSubject
     * @param serviceSubject
     * @return
     * @throws AuthException 
     */
    @Override
    public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject) throws AuthException {
        System.out.println("ValidateRequest Called. ");
        return AuthStatus.SUCCESS;
    }

    /**
     * Returns SUCCESS if a response is deemed secure.
     * @param messageInfo
     * @param serviceSubject
     * @return
     * @throws AuthException 
     */
    @Override
    public AuthStatus secureResponse(MessageInfo messageInfo, Subject serviceSubject) throws AuthException {
        return AuthStatus.SEND_SUCCESS;
    }

    /**
     * Clear all principals from the subject
     * @param messageInfo
     * @param subject
     * @throws AuthException 
     */
    @Override
    public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
