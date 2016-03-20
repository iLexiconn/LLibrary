package net.ilexiconn.llibrary.server.command.argument;

/**
 * @author iLexiconn
 * @since 1.0.0
 */
public class Argument {
    private String name;
    private String value;
    private ArgumentTypes type;

    public Argument(String name, String value, ArgumentTypes type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public ArgumentTypes getType() {
        return type;
    }
}
