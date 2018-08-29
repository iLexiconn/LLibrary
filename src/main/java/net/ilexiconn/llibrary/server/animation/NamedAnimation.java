package net.ilexiconn.llibrary.server.animation;

public class NamedAnimation extends Animation {
    private final String name;

    private NamedAnimation(String animationName, int duration) {
        super(duration);
        this.name = animationName;
    }

    public String getName() {
        return name;
    }

    public static NamedAnimation create(String name, int duration) {
        return new NamedAnimation(name, duration);
    }
}
