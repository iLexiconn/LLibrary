package net.ilexiconn.llibrary.server.snackbar;

public class Snackbar {
    private String message;
    private int duration;

    private Snackbar(String message) {
        this.message = message;
        this.duration = 0;
    }

    public static Snackbar create(String message) {
        return new Snackbar(message);
    }

    public String getMessage() {
        return message;
    }

    public Snackbar setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    public int getDuration() {
        return this.duration;
    }
}
