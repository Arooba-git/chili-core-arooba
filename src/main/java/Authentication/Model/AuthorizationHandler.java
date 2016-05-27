package Authentication.Model;

import java.util.HashMap;

/**
 * @author Robin Duda
 */
public class AuthorizationHandler<T> {
    private HashMap<String, T> authorized = new HashMap<>();
    private HashMap<String, T> unauthorized = new HashMap<>();
    private Access access;

    public AuthorizationHandler(Access access) {
        this.access = access;
    }

    public void use(String action, T handler, Access access) {
        switch (access) {
            case PUBLIC:
                unauthorized.put(action, handler);
                break;
            case AUTHORIZE:
                authorized.put(action, handler);
                break;
        }
    }

    public void use(String action, T handler) {
        use(action, handler, access);
    }

    public T get(String action, Access access) throws AuthorizationRequired, HandlerMissingException {
        switch (access) {
            case PUBLIC:
                return unauthorized(action);
            case AUTHORIZE:
                return any(action);
            default:
                throw new AuthorizationRequired();
        }
    }

    private T unauthorized(String action) throws AuthorizationRequired, HandlerMissingException {
        if (unauthorized.containsKey(action)) {
            return unauthorized.get(action);
        } else if (authorized.containsKey(action)) {
            throw new AuthorizationRequired();
        } else {
            throw new HandlerMissingException();
        }
    }

    private T any(String action) throws HandlerMissingException {
        if (authorized.containsKey(action)) {
            return authorized.get(action);
        } else if (unauthorized.containsKey(action)) {
            return unauthorized.get(action);
        } else {
            throw new HandlerMissingException();
        }
    }

    public enum Access {
        PUBLIC, AUTHORIZE
    }
}
