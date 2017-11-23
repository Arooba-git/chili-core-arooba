package com.codingchili.core.listener;

import com.codingchili.core.context.StartupListener;
import com.codingchili.core.protocol.Serializer;
import io.vertx.core.Future;
import io.vertx.core.eventbus.DeliveryOptions;
import io.vertx.core.json.JsonObject;

import java.util.Map;

/**
 * A clustered session that can be used to write directly to a listener at the
 * edge of the cluster.
 */
class ClusteredSession implements Session {
    private JsonObject data = new JsonObject();
    private DeliveryOptions delivery = new DeliveryOptions();
    private SessionFactory sessionFactory;

    public ClusteredSession() {
        StartupListener.subscibe(core -> {
            ClusteredSessionFactory.get(core).setHandler(get -> {
                // this is synchronous; we can never get a session without first loading up the factory.
                // (unless someone calls the constructor... )
                // its a hack but it works right now. needs to be persisted and retrieved
                // which does not work for the stateful factory. need to redesign this dependency.
                sessionFactory = get.result();
            });
        });
    }

    public ClusteredSession(SessionFactory factory, String source, String connection) {
        this.sessionFactory = factory;

        setData(new JsonObject()
                .put(SOURCE, source)
                .put(CONNECTION, connection).getMap());
        update();
    }

    @Override
    public Future<Boolean> isActive() {
        return sessionFactory.isActive(this);
    }

    @Override
    public Future<Void> destroy() {
        return sessionFactory.destroy(this);
    }

    @Override
    public String source() {
        return data.getString(SOURCE);
    }

    @Override
    public String connection() {
        return data.getString(CONNECTION);
    }

    @Override
    public JsonObject data() {
        return data;
    }

    @Override
    public Future<Void> update() {
        return sessionFactory.update(this);
    }

    @Override
    public void write(Object object) {
        sessionFactory.context().bus().send(source(), Serializer.json(object), delivery);
    }

    @Override
    public String id() {
        return connection() + "@" + source();
    }

    /**
     * Getter to support serialization.
     * @return json object as map.
     */
    public Map<String, Object> getData() {
        return data.getMap();
    }

    /**
     * setter to support serialiation.
     *
     * @param data the data to set for the session.
     */
    public void setData(Map<String, Object> data) {
        this.data = new JsonObject(data);
        delivery = new DeliveryOptions()
                .addHeader(SOURCE, source())
                .addHeader(CONNECTION, connection());
    }

    public void setSessionFactory(SessionFactory factory) {
        this.sessionFactory = factory;
    }

    @Override
    public int hashCode() {
        return id().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return compareTo(obj) == 0;
    }
}
