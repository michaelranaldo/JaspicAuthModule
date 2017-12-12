package uk.co.ranaldo.playground.jaspicauthmodule;

import java.util.Map;
import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.message.AuthException;
import javax.security.auth.message.AuthStatus;
import javax.security.auth.message.MessageInfo;
import javax.security.auth.message.MessagePolicy;
import javax.security.auth.message.callback.CallerPrincipalCallback;
import javax.security.auth.message.callback.GroupPrincipalCallback;
import javax.security.auth.message.module.ServerAuthModule;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Michael Ranaldo <michael@ranaldo.co.uk>
 */
public class JaspicAuthModule implements ServerAuthModule {

    /**
     * List the message types which we respond to (ie only Http requests and responses delivered via a servlet). Hide
     * the warnings from Request and Response as we're dealing with a basic application.
     *
     */
    @SuppressWarnings("rawtypes")
    protected static final Class[] SUPPORTED_MESSAGE_TYPES = new Class[]{
        HttpServletRequest.class, HttpServletResponse.class};

    private CallbackHandler handler;

    /**
     * Initialize our module (constructor).
     *
     * @param requestPolicy
     * @param responsePolicy
     * @param handler
     * @param options
     * @throws AuthException
     */
    @Override
    public void initialize(MessagePolicy requestPolicy, MessagePolicy responsePolicy, CallbackHandler handler,
            Map options) throws AuthException {
        System.out.println("Initialize Called.");
        this.handler = handler;
    }

    /**
     * Return the HTTP Servlet Request and Response types.
     *
     * @return [ request.class, response.class ]
     */
    @SuppressWarnings("rawtypes")
    @Override
    public Class[] getSupportedMessageTypes() {
        return SUPPORTED_MESSAGE_TYPES;
    }

    /**
     * Main method: In order to pass on the user and role, this example has them as servlet request parameters. This
     * method extricates the user and role from the servlet parameters to authenticate them via authenticateUser.
     *
     * @param messageInfo
     * @param clientSubject
     * @param serviceSubject
     * @return
     * @throws AuthException
     */
    @Override
    public AuthStatus validateRequest(MessageInfo messageInfo, Subject clientSubject, Subject serviceSubject)
            throws AuthException {
        System.out.println("ValidateRequest Called. ");
        HttpServletRequest request = (HttpServletRequest) messageInfo.getRequestMessage();

        String user = request.getParameter("user");
        String group = request.getParameter("group");

        System.out.println("User = " + user);
        System.out.println("Group = " + group);

        authenticateUser(user, group, clientSubject, serviceSubject);

        return AuthStatus.SUCCESS;
    }

    /**
     * Returns SUCCESS if a response is deemed secure.
     *
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
     * Clear all principals from the subject.
     *
     * @param messageInfo
     * @param subject
     * @throws AuthException
     */
    @Override
    public void cleanSubject(MessageInfo messageInfo, Subject subject) throws AuthException {
        if (subject != null) {
            subject.getPrincipals().clear();
        }
    }

    /**
     *
     */
    private void authenticateUser(String user, String group, Subject clientSubject, Subject serverSubject) {
        System.out.println("Authenticating user " + user + " in group " + group);
        CallerPrincipalCallback callerPrincipalCallback = new CallerPrincipalCallback(clientSubject, user);
        GroupPrincipalCallback groupPrincipalCallback = new GroupPrincipalCallback(clientSubject, new String[]{group});

        try {
            handler.handle(new Callback[]{callerPrincipalCallback, groupPrincipalCallback});
        } catch (Exception godException) {
            System.out.println(godException.getMessage());
        }
    }
}
