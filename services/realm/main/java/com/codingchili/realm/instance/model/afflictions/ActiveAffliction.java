package com.codingchili.realm.instance.model.afflictions;

import com.codingchili.realm.instance.context.GameContext;
import com.codingchili.realm.instance.model.entity.Creature;
import com.codingchili.realm.instance.scripting.Bindings;
import com.codingchili.realm.instance.model.stats.Attribute;
import com.codingchili.realm.instance.model.stats.Stats;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Robin Duda
 * <p>
 * Maps an active affliction onto an entity.
 */
public class ActiveAffliction extends Affliction {
    private Stats stats = new Stats();
    private Affliction affliction;
    private Creature source;
    private Creature target;
    private Integer ticks;
    private Long start = System.currentTimeMillis();

    public ActiveAffliction(Creature source, Creature target, Affliction affliction) {
        this.source = source;
        this.target = target;
        this.affliction = affliction;
        this.ticks = affliction.duration;
    }

    public Stats modify(GameContext context) {
        stats.clear();
        affliction.apply(getBindings(context).setStats(stats));
        return stats;
    }

    @JsonIgnore
    public Long getStart() {
        return start;
    }

    /**
     * @param context the game context that the target exists within.
     * @return true if still active.
     */
    public boolean tick(GameContext context) {
        affliction.tick(getBindings(context));

        return (--ticks > 0);
    }

    private Bindings getBindings(GameContext context) {
        return new Bindings()
                .setContext(context)
                .setAffliction(affliction)
                .setAttribute(Attribute.class);
    }

    public Affliction getAffliction() {
        return affliction;
    }

    public void setAffliction(Affliction affliction) {
        this.affliction = affliction;
    }

    public Creature getSource() {
        return source;
    }

    public void setSource(Creature source) {
        this.source = source;
    }

    public Creature getTarget() {
        return target;
    }

    public void setTarget(Creature target) {
        this.target = target;
    }
}
