package net.ilexiconn.llibrary.client.model.tabula.container;

import java.util.List;
import java.util.Map;

public class TabulaAnimationContainer {
    private String name;
    private String identifier;

    private boolean loops;

    private Map<String, List<TabulaAnimationComponentContainer>> sets;

    public String getName() {
        return this.name;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public boolean doesLoop() {
        return this.loops;
    }

    public Map<String, List<TabulaAnimationComponentContainer>> getComponents() {
        return this.sets;
    }
}
