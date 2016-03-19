package net.ilexiconn.llibrary.server.animation;

public class Animation {
    private int id;
    private int duration;

    private Animation(int id, int duration) {
        this.id = id;
        this.duration = duration;
    }

    public static Animation create(int id, int duration) {
        return new Animation(id, duration);
    }

    public int getID() {
        return id;
    }

    public int getDuration() {
        return duration;
    }
}
