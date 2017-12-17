package com.codingchili.realm.instance.model.entity;

import com.codingchili.realm.instance.model.events.Event;

import com.codingchili.core.protocol.Protocol;
import com.codingchili.core.protocol.RoleMap;

/**
 * @author Robin Duda
 */
public class EventProtocol extends Protocol<Event> {
    private Integer id;

    public EventProtocol(Entity entity) {
        this.id = entity.getId().hashCode();
        setRole(RoleMap.get(RoleMap.PUBLIC));
        annotated(entity);
    }

    public Integer getId() {
        return id;
    }
}
