package net.ilexiconn.llibrary.server.snackbar;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public class Snackbar {
    private String message;
    private int duration;

    private Snackbar(String message) {
        this.message = message;
        this.duration = 0;
    }

    /**
     * Creae a new snackbar nstance. Every snackbar instance can be shown multiple times.
     *
     * @param message the message to display
     * @return the new snackbar instance
     */
    public static Snackbar create(String message) {
        return new Snackbar(message);
    }

    /**
     * @return this snackbar's message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set a custom duration for this snackbar. If it's 0 (default), the text lenght * 2 will be used.
     *
     * @param duration the custom curation
     * @return this snackbar instance
     */
    public Snackbar setDuration(int duration) {
        this.duration = duration;
        return this;
    }

    /**
     * @return this snackbar's duration
     */
    public int getDuration() {
        return this.duration;
    }
}
