package net.ilexiconn.llibrary.server.animation;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public class Animation {
    private int id;
    private int duration;

    private Animation(int id, int duration) {
        this.id = id;
        this.duration = duration;
    }

    /**
     * @param id the animation id
     * @param duration the animation duration
     * @return an animation with the given id and duration
     */
    public static Animation create(int id, int duration) {
        return new Animation(id, duration);
    }

    /**
     * @return the id of this animation
     */
    public int getID() {
        return id;
    }

    /**
     * @return the duration of this animation
     */
    public int getDuration() {
        return duration;
    }
}
