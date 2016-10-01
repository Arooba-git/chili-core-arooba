package Routing.Controller.Transport;

import Authentication.Controller.RealmRequest;
import Configuration.Strings;
import Protocols.Util.Token;
import Protocols.Util.Serializer;
import Realm.Configuration.RealmSettings;
import io.vertx.core.json.JsonObject;

import static Configuration.Strings.*;

/**
 * @author Robin Duda
 */

class RealmWebsocketRequest implements RealmRequest {
    private RealmConnection connection;
    private JsonObject data;
    private JsonObject realm;

    RealmWebsocketRequest(RealmConnection connection, JsonObject data) {
        this.connection = connection;
        this.data = data;
        this.realm = data.getJsonObject(ID_REALM);
    }

    RealmWebsocketRequest(RealmConnection connection, String action) {
        this(connection, new JsonObject().put(Strings.ID_ACTION, action));
    }

    @Override
    public RealmSettings realm() {
        return Serializer.unpack(realm, RealmSettings.class);
    }

    @Override
    public int players() {
        return data.getInteger(ID_PLAYERS);
    }

    @Override
    public void write(Object object) {
        connection.write(object);
    }

    @Override
    public void error() {
        connection.write(new JsonObject().put(PROTOCOL_ACCEPTED, false).encode());
    }

    @Override
    public String realmName() {
        return connection.realm();
    }

    @Override
    public String sender() {
        return data.getString(PROTOCOL_CONNECTION);
    }


    @Override
    public RealmConnection connection() {
        return connection;
    }

    @Override
    public void accept() {
        connection.write(new JsonObject().put(PROTOCOL_ACCEPTED, true).encode());
    }

    @Override
    public void missing() {
        connection.write(new JsonObject().put(PROTOCOL_ERROR, true));
    }

    @Override
    public void conflict() {

    }

    @Override
    public String action() {
        return data.getString(ID_ACTION);
    }

    @Override
    public String target() {
        return null;
    }

    @Override
    public Token token() {
        JsonObject authentication = realm.getJsonObject(PROTOCOL_AUTHENTICATION);

        if (authentication.containsKey(ID_TOKEN)) {
            return Serializer.unpack(authentication.getJsonObject(ID_TOKEN), Token.class);
        } else {
            return null;
        }
    }

    @Override
    public JsonObject data() {
        return data;
    }

    @Override
    public int timeout() {
        return 0;
    }

    @Override
    public String account() {
        return data.getString(ID_ACCOUNT);
    }

    @Override
    public String name() {
        return data.getString(ID_NAME);
    }

    @Override
    public void unauthorized() {
        connection.write(new JsonObject().put(PROTOCOL_ERROR, true));
    }
}
