package Authentication.Configuration;

import Configuration.Configurable;
import Configuration.RemoteAuthentication;

import static Configuration.Strings.PATH_AUTHSERVER;


public class AuthServerSettings implements Configurable {
    private RemoteAuthentication logserver;
    private byte[] clientSecret;
    private byte[] realmSecret;
    private Integer realmPort;
    private Integer clientPort;
    private DatabaseSettings database;
    private String[] realms;

    public boolean isTrustedRealm(String name) {
        boolean result = false;

        for (Object object : realms) {
            if (object.equals(name))
                result = true;
        }

        return result;
    }

    @Override
    public String getPath() {
        return PATH_AUTHSERVER;
    }

    @Override
    public String getName() {
        return logserver.getSystem();
    }


    public String[] getRealms() {
        return realms;
    }

    protected void setRealms(String[] realms) {
        this.realms = realms;
    }

    @Override
    public RemoteAuthentication getLogserver() {
        return logserver;
    }

    protected void setLogserver(RemoteAuthentication logserver) {
        this.logserver = logserver;
    }

    public DatabaseSettings getDatabase() {
        return database;
    }

    protected void setDatabase(DatabaseSettings database) {
        this.database = database;
    }

    public Integer getClientPort() {
        return clientPort;
    }

    protected void setClientPort(Integer clientPort) {
        this.clientPort = clientPort;
    }

    public Integer getRealmPort() {
        return realmPort;
    }

    protected void setRealmPort(Integer realmPort) {
        this.realmPort = realmPort;
    }

    public byte[] getRealmSecret() {
        return realmSecret;
    }

    public void setRealmSecret(byte[] realmSecret) {
        this.realmSecret = realmSecret;
    }

    public byte[] getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(byte[] clientSecret) {
        this.clientSecret = clientSecret;
    }
}
